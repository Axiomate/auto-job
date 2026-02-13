package com.axiom.auto_job.agent;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import com.axiom.auto_job.contract.Agent;
import com.axiom.auto_job.record.JobInfo;
import com.axiom.auto_job.record.ResumeTailorInput;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ResumeTailorAgent implements Agent<ResumeTailorInput, File> {

    private final ChatClient chatClient;

    @Override
    public File execute(ResumeTailorInput input) {

        try {
            String baseResume = readFile(input.baseResume());
            String prompt = buildPrompt(input.job(), baseResume);

            // Call AI to tailor resume
            String tailoredText = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            // Ensure resumes folder exists
            File outputDir = new File("resumes");
            if (!outputDir.exists()) outputDir.mkdirs();

            File output = new File(outputDir, "tailored-" + sanitizeFileName(input.job().companyName()) + ".pdf");

            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                    contentStream.beginText();
                    contentStream.setLeading(14.5f);
                    contentStream.newLineAtOffset(50, 750);

                    for (String line : tailoredText.split("\n")) {
                        contentStream.showText(line);
                        contentStream.newLine();
                    }

                    contentStream.endText();
                }

                document.save(output);
            }

            return output;

        } catch (Exception e) {
            throw new RuntimeException("Failed to tailor resume", e);
        }
    }

    private String buildPrompt(JobInfo job, String resumeText) {
        return """
                You are an AI resume assistant.

                Tailor this resume for the following job.

                Job Title: %s
                Company: %s
                Job Description: %s

                Base Resume:
                %s

                Rewrite the resume:
                - Keep relevant experience
                - Highlight matching skills
                - Use professional formatting
                - Output as plain text
                """.formatted(job.jobTitle(), job.companyName(), job.description(), resumeText);
    }

    private String readFile(File file) throws Exception {
        return new String(FileCopyUtils.copyToByteArray(file), StandardCharsets.UTF_8);
    }

    private String sanitizeFileName(String name) {
        return name.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
