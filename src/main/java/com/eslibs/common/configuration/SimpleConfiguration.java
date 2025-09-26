package com.eslibs.common.configuration;

import com.eslibs.common.configuration.connection.IConnection;
import com.eslibs.common.configuration.credentials.ICredentials;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
@Jacksonized
@SuperBuilder
@EqualsAndHashCode
public class SimpleConfiguration implements IConfiguration {

    protected final IConnection connection;
    protected final ICredentials credentials;
}
