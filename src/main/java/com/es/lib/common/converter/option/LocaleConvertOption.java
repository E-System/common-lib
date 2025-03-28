package com.es.lib.common.converter.option;

import com.es.lib.common.converter.ConvertOption;
import lombok.Data;

@Data
public class LocaleConvertOption implements ConvertOption {

    private final String locale;
}