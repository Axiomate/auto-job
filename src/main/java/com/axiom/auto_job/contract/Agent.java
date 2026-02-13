package com.axiom.auto_job.contract;
@FunctionalInterface
public interface Agent<I, O> {
    O execute(I input) throws Exception;
}
