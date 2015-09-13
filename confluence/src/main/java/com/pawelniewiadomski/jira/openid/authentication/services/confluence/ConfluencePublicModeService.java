package com.pawelniewiadomski.jira.openid.authentication.services.confluence;

import com.atlassian.confluence.user.SignupManager;
import com.atlassian.fugue.Option;
import com.atlassian.plugin.spring.scanner.annotation.component.ConfluenceComponent;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.pawelniewiadomski.jira.openid.authentication.services.PublicModeService;
import lombok.AllArgsConstructor;

import java.util.List;

import static org.apache.commons.lang.StringUtils.split;

@ConfluenceComponent
@AllArgsConstructor
public class ConfluencePublicModeService implements PublicModeService {
    @ComponentImport
    final SignupManager signupManager;

    @Override
    public boolean canAnyoneSignUp() {
        return signupManager.isPublicSignupPermitted();
    }

    @Override
    public Option<List<String>> getAllowedDomains() {
        final Option<List<String>> option = Option.some(split(signupManager.getRestrictedDomains(), ','))
                .map(new Function<String[], List<String>>() {
                    @Override
                    public ImmutableList<String> apply(String[] elements) {
                        return ImmutableList.copyOf(elements);
                    }
                });

        return option.<List<String>>orElse(new Supplier<Option<List<String>>>() {
            @Override
            public Option<List<String>> get() {
                return Option.<List<String>>some(ImmutableList.<String>of());
            }
        });
    }
}
