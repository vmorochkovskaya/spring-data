package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FolderStatistic {
    private int filesCount;
    private int foldersCount;
    private long allFilesSize;
}
