package com.eslibs.common.file;

public enum FileType {
    WORD,
    EXCEL,
    PDF {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    TEXT,
    ARCHIVE,
    IMAGE {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    CODE,
    VIDEO {
        @Override
        public boolean isCanOpen() {
            return true;
        }
    },
    POWERPOINT,
    OTHER {
        @Override
        public String getIcon() {
            return "file-o";
        }
    };

    public String getIcon() {
        return "file-" + toString().toLowerCase() + "-o";
    }

    public boolean isCanOpen() {
        return false;
    }
}