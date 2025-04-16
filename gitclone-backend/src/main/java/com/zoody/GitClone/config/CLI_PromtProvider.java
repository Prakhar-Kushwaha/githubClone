package com.zoody.GitClone.config;

import org.jline.utils.AttributedString;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class CLI_PromtProvider implements PromptProvider {


    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Shell (:>");
    }
}
