package com.axiom.auto_job.agent.scraper;

import com.axiom.auto_job.contract.JobScraper;
import com.axiom.auto_job.record.JobInfo;
import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LinkedInPlaywrightScraper implements JobScraper {

    private static final String STORAGE = "linkedin-session.json";

    @Override
    public List<JobInfo> scrape() {

        List<JobInfo> jobs = new ArrayList<>();

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false) // better for avoiding detection
                            .setSlowMo(200)
            );

            BrowserContext context;

            // reuse login session if exists
            if (Paths.get(STORAGE).toFile().exists()) {
                context = browser.newContext(
                        new Browser.NewContextOptions()
                                .setStorageStatePath(Paths.get(STORAGE))
                );
            } else {
                context = browser.newContext();
            }

            Page page = context.newPage();

            loginIfNeeded(page);

            String searchUrl =
                    "https://www.linkedin.com/jobs/search/?keywords=java%20spring%20boot&location=United%20Arab%20Emirates";

            page.navigate(searchUrl);

            page.waitForTimeout(5000);

            autoScroll(page);

            var cards = page.locator(".jobs-search-results__list-item");

            int count = cards.count();

            for (int i = 0; i < count; i++) {

                cards.nth(i).click();
                page.waitForTimeout(1500);

                String title =
                        safeText(page, ".job-details-jobs-unified-top-card__job-title");

                String company =
                        safeText(page, ".job-details-jobs-unified-top-card__company-name");

                String desc =
                        safeText(page, ".jobs-description-content__text");

                jobs.add(new JobInfo(
                        null,
                        company,
                        title,
                        desc,
                        guessEmail(company)
                ));
            }

            context.storageState(
                    new BrowserContext.StorageStateOptions()
                            .setPath(Paths.get(STORAGE))
            );

            browser.close();

        } catch (Exception e) {
            log.error("LinkedIn scrape failed", e);
        }

        return jobs;
    }

    // -----------------------

    private void loginIfNeeded(Page page) {

        page.navigate("https://www.linkedin.com/login");

        if (page.url().contains("login")) {

            log.info("Manual login required (first time only)");

            // Wait for manual login
            page.waitForURL("https://www.linkedin.com/feed/", 
                    new Page.WaitForURLOptions().setTimeout(180_000));
        }
    }

    private void autoScroll(Page page) {

        for (int i = 0; i < 6; i++) {
            page.mouse().wheel(0, 3000);
            page.waitForTimeout(1500);
        }
    }

    private String safeText(Page page, String selector) {
        try {
            return page.locator(selector).innerText();
        } catch (Exception e) {
            return "";
        }
    }

    private String guessEmail(String company) {
        return "hr@" + company.toLowerCase().replace(" ", "") + ".com";
    }
}

