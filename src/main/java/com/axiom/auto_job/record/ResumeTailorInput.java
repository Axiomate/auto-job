package com.axiom.auto_job.record;
import java.io.File;


public record ResumeTailorInput(
        JobInfo job,
        File baseResume
) {}

