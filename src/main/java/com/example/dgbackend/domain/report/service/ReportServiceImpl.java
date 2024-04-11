package com.example.dgbackend.domain.report.service;

import com.example.dgbackend.domain.combination.Combination;
import com.example.dgbackend.domain.combination.repository.CombinationRepository;
import com.example.dgbackend.domain.combinationcomment.CombinationComment;
import com.example.dgbackend.domain.combinationcomment.repository.CombinationCommentRepository;
import com.example.dgbackend.domain.member.Member;
import com.example.dgbackend.domain.member.repository.MemberRepository;
import com.example.dgbackend.domain.recipe.Recipe;
import com.example.dgbackend.domain.recipe.repository.RecipeRepository;
import com.example.dgbackend.domain.recipecomment.RecipeComment;
import com.example.dgbackend.domain.recipecomment.repository.RecipeCommentRepository;
import com.example.dgbackend.domain.report.Report;
import com.example.dgbackend.domain.report.dto.ReportRequest.ReportReq;
import com.example.dgbackend.domain.report.repository.ReportRepository;
import com.example.dgbackend.global.common.response.code.status.ErrorStatus;
import com.example.dgbackend.global.exception.ApiException;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final JavaMailSender javaMailSender;
	private final ReportRepository reportRepository;
	private final MemberRepository memberRepository;
	private final CombinationRepository combinationRepository;
	private final CombinationCommentRepository combinationCommentRepository;
	private final RecipeRepository recipeRepository;
	private final RecipeCommentRepository recipeCommentRepository;

	private static final String dgEmail = "drinkgourmet.official@gmail.com";

	@Transactional
	public String report(ReportReq reportReq, Member member) throws MessagingException {

		Report prevReport = reportRepository.findTopByResourceIdAndReportTargetAndMember(
			reportReq.getResourceId(), reportReq.getReportTarget(), member);

		duplicateReport(prevReport);

		Report report = Report.toEntity(reportReq, member);

		reportRepository.save(report);

		sortReport(reportReq, member);

		return "신고 완료";
	}

	private void duplicateReport(Report prevReport) {
		if (prevReport != null) {
			throw new ApiException(ErrorStatus._DUPLICATE_REPORT);
		}
	}

	private void sortReport(ReportReq reportReq, Member member) throws MessagingException {
		switch (reportReq.getReportTarget()) {
			case COMBINATION -> {
				Combination combination = combinationRepository.findByIdAndStateIsTrue(
						reportReq.getResourceId())
					.orElseThrow(() -> new ApiException(ErrorStatus._COMBINATION_NOT_FOUND));

				combination.delete();
				sendReportMail(reportReq, member);

			}
			case RECIPE -> {
				Recipe recipe = recipeRepository.findByIdAndStateIsTrue(reportReq.getResourceId())
					.orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE));

				recipe.delete();
				sendReportMail(reportReq, member);
			}
			case COMBINATION_COMMENT -> {
				CombinationComment combinationComment = combinationCommentRepository.findByIdAndStateIsTrue(
						reportReq.getResourceId())
					.orElseThrow(
						() -> new ApiException(ErrorStatus._COMBINATION_COMMENT_NOT_FOUND));

				combinationComment.updateState();
				sendReportMail(reportReq, member);
			}
			case RECIPE_COMMENT -> {
				RecipeComment recipeComment = recipeCommentRepository.findByIdAndStateIsTrue(
						reportReq.getResourceId())
					.orElseThrow(() -> new ApiException(ErrorStatus._EMPTY_RECIPE_COMMENT));
				recipeComment.updateState();
				sendReportMail(reportReq, member);
			}
		}
	}

	private void sendReportMail(ReportReq reportReq, Member member) throws MessagingException {
		MimeMessage mail = javaMailSender.createMimeMessage();
		//신고한 사람
		long reportMember = member.getId();
		mail.setSubject("[음주미식회] " + reportMember + "번 회원님의 신고로 인한 임시 정지/삭제", "utf-8");
		mail.setText(createReportEmailContent(reportReq, member), "utf-8",
			"html");
		mail.addRecipient(RecipientType.TO, new InternetAddress(dgEmail));
		javaMailSender.send(mail);

	}

	private String createReportEmailContent(ReportReq reportReq, Member member) {
		long reportMember = member.getId();
		long resourceId = reportReq.getResourceId();
		String reportTarget = reportReq.getReportTarget().getName();
		String content = reportReq.getContent(); // 신고 사유(내용)
		String reportReason = reportReq.getReportReason().getName();
		String suspensionDuration = reportReq.getReportReason().getSuspensionDuration();
		String reportContent = reportReq.getReportContent(); // 신고 대상 내용

		return
			"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n"
				+ "<head>\n"
				+ "<!--[if gte mso 9]>\n"
				+ "<xml>\n"
				+ "  <o:OfficeDocumentSettings>\n"
				+ "    <o:AllowPNG/>\n"
				+ "    <o:PixelsPerInch>96</o:PixelsPerInch>\n"
				+ "  </o:OfficeDocumentSettings>\n"
				+ "</xml>\n"
				+ "<![endif]-->\n"
				+ "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
				+ "  <meta name=\"x-apple-disable-message-reformatting\">\n"
				+ "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n"
				+ "  <title></title>\n"
				+ "  \n"
				+ "    <style type=\"text/css\">\n"
				+ "      @media only screen and (min-width: 620px) {\n"
				+ "  .u-row {\n"
				+ "    width: 600px !important;\n"
				+ "  }\n"
				+ "  .u-row .u-col {\n"
				+ "    vertical-align: top;\n"
				+ "  }\n"
				+ "\n"
				+ "  .u-row .u-col-100 {\n"
				+ "    width: 600px !important;\n"
				+ "  }\n"
				+ "\n"
				+ "}\n"
				+ "\n"
				+ "@media (max-width: 620px) {\n"
				+ "  .u-row-container {\n"
				+ "    max-width: 100% !important;\n"
				+ "    padding-left: 0px !important;\n"
				+ "    padding-right: 0px !important;\n"
				+ "  }\n"
				+ "  .u-row .u-col {\n"
				+ "    min-width: 320px !important;\n"
				+ "    max-width: 100% !important;\n"
				+ "    display: block !important;\n"
				+ "  }\n"
				+ "  .u-row {\n"
				+ "    width: 100% !important;\n"
				+ "  }\n"
				+ "  .u-col {\n"
				+ "    width: 100% !important;\n"
				+ "  }\n"
				+ "  .u-col > div {\n"
				+ "    margin: 0 auto;\n"
				+ "  }\n"
				+ "}\n"
				+ "body {\n"
				+ "  margin: 0;\n"
				+ "  padding: 0;\n"
				+ "}\n"
				+ "\n"
				+ "table,\n"
				+ "tr,\n"
				+ "td {\n"
				+ "  vertical-align: top;\n"
				+ "  border-collapse: collapse;\n"
				+ "}\n"
				+ "\n"
				+ "p {\n"
				+ "  margin: 0;\n"
				+ "}\n"
				+ "\n"
				+ ".ie-container table,\n"
				+ ".mso-container table {\n"
				+ "  table-layout: fixed;\n"
				+ "}\n"
				+ "\n"
				+ "* {\n"
				+ "  line-height: inherit;\n"
				+ "}\n"
				+ "\n"
				+ "a[x-apple-data-detectors='true'] {\n"
				+ "  color: inherit !important;\n"
				+ "  text-decoration: none !important;\n"
				+ "}\n"
				+ "\n"
				+ "table, td { color: #000000; } </style>\n"
				+ "  \n"
				+ "  \n"
				+ "\n"
				+ "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->\n"
				+ "\n"
				+ "</head>\n"
				+ "\n"
				+ "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #f9f9f9;color: #000000\">\n"
				+ "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n"
				+ "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n"
				+ "  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #f9f9f9;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n"
				+ "  <tbody>\n"
				+ "  <tr style=\"vertical-align: top\">\n"
				+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n"
				+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #f9f9f9;\"><![endif]-->\n"
				+ "    \n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #f9f9f9;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "<table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
				+ "  <tbody>\n"
				+ "    <tr>\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:15px;font-family:'Lato',sans-serif;\" align=\"left\">\n"
				+ "        \n"
				+ "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #f9f9f9;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n"
				+ "    <tbody>\n"
				+ "      <tr style=\"vertical-align: top\">\n"
				+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n"
				+ "          <span>&#160;</span>\n"
				+ "        </td>\n"
				+ "      </tr>\n"
				+ "    </tbody>\n"
				+ "  </table>\n"
				+ "\n"
				+ "      </td>\n"
				+ "    </tr>\n"
				+ "  </tbody>\n"
				+ "</table>\n"
				+ "\n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffb97a;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffb97a;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "<table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
				+ "  <tbody>\n"
				+ "    <tr>\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:35px 10px 10px;font-family:'Lato',sans-serif;\" align=\"left\">\n"
				+ "        \n"
				+ "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
				+ "  <tr>\n"
				+ "    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n"
				+ "      \n"
				+ "      <img align=\"center\" border=\"0\" src=\"https://assets.unlayer.com/projects/0/1712749783353-음주미식회.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 10%;max-width: 58px;\" width=\"58\"/>\n"
				+ "      \n"
				+ "    </td>\n"
				+ "  </tr>\n"
				+ "</table>\n"
				+ "\n"
				+ "      </td>\n"
				+ "    </tr>\n"
				+ "  </tbody>\n"
				+ "</table>\n"
				+ "\n"
				+ "<table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
				+ "  <tbody>\n"
				+ "    <tr>\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 30px;font-family:'Lato',sans-serif;\" align=\"left\">\n"
				+ "        \n"
				+ "  <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"font-size: 28px; line-height: 39.2px; color: #ffffff; font-family: Lato, sans-serif;\">신고로 인한 임시 삭제 안내</span></p>\n"
				+ "  </div>\n"
				+ "\n"
				+ "      </td>\n"
				+ "    </tr>\n"
				+ "  </tbody>\n"
				+ "</table>\n"
				+ "\n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "<table style=\"font-family:'Lato',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n"
				+ "  <tbody>\n"
				+ "    <tr>\n"
				+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:40px 40px 30px;font-family:'Lato',sans-serif;\" align=\"left\">\n"
				+ "        \n"
				+ "  <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n"
				+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">음주 미식회 신고 안내</span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"> </p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\"> ["
				+ String.valueOf(reportMember) + "]번 회원님이 [" + String.valueOf(resourceId)
				+ "]번 게시물 또는 댓글에 대해 신고를 하여 임시 삭제처리 되었습니다.<br /><br />해당 종류 : " + reportTarget
				+ "</span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">해당 내용 : "
				+ reportContent + " </span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">신고 사유 : "
				+ content + " </span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">삭제 이유 : "
				+ reportReason + "</span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">정지 기간 : "
				+ suspensionDuration + "<br /><br /></span></p>\n"
				+ "<p style=\"font-size: 14px; line-height: 140%;\"><span style=\"font-size: 18px; line-height: 25.2px; color: #666666;\">따라서 자동으로 숨김 및 삭제 처리가 되었음을 알려드립니다.</span></p>\n"
				+ "  </div>\n"
				+ "\n"
				+ "      </td>\n"
				+ "    </tr>\n"
				+ "  </tbody>\n"
				+ "</table>\n"
				+ "\n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #f9f9f9\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #1c103b;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #f9f9f9;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #1c103b;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "  \n"
				+ "  \n"
				+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n"
				+ "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #f9f9f9;\">\n"
				+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n"
				+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #f9f9f9;\"><![endif]-->\n"
				+ "      \n"
				+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n"
				+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n"
				+ "  <div style=\"height: 100%;width: 100% !important;\">\n"
				+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n"
				+ "  \n"
				+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n"
				+ "  </div>\n"
				+ "</div>\n"
				+ "<!--[if (mso)|(IE)]></td><![endif]-->\n"
				+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n"
				+ "    </div>\n"
				+ "  </div>\n"
				+ "  </div>\n"
				+ "  \n"
				+ "\n"
				+ "\n"
				+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n"
				+ "    </td>\n"
				+ "  </tr>\n"
				+ "  </tbody>\n"
				+ "  </table>\n"
				+ "  <!--[if mso]></div><![endif]-->\n"
				+ "  <!--[if IE]></div><![endif]-->\n"
				+ "</body>\n"
				+ "\n"
				+ "</html>\n";
	}


}
