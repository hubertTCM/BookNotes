# -*- coding: utf-8 -*-
import codecs
import sys
import os
import re

# reload(sys)
def parse_prescription(source, to):
    source_file = codecs.open(source, 'r', 'utf-8', 'ignore')
    to_file = codecs.open(to, 'w+', 'utf-8')
    
    # 炙甘草汤（又名复脉汤）
    # 炙草 桂枝 人参 麻仁 生地 阿胶 麦冬 生姜 大枣
    
    # 桂枝汤
    # 桂枝 白芍 炙草 生姜 大枣
    
    #桂枝加附子汤 即桂枝汤加附子。
    for line in source_file:
        line = line.strip()
        if (line.startswith("<D>")):
            continue
        to_file.write(line + "\n")
        
    to_file.write("hello world")
    source_file.close()
    to_file.close()
    
    print (to)

if __name__ == "__main__":
    source_file = os.path.abspath("../resource/集方.txt")
    to_file = os.path.abspath("../resource/常见处方.txt")

    parse_prescription(source_file, to_file)
    print("done")
    pass
