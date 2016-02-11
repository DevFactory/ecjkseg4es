# ECJKSeg
又一个基于mmseg的cjk中文分词器，首先按照Unicode text segmentation, aux #29 的国际标准分词，即首先切分英文与数字等，再在切出的中文串句子上采用MMSEG分词，词典使用搜狗分词库。
