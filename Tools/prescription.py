# -*- coding: utf-8 -*-
import codecs
import sys
import os
import re


# reload(sys)
def parse_prescription(source, to):
    source_file = codecs.open(source, 'r', 'utf-8', 'ignore')
    to_file = codecs.open(to, 'w+', 'utf-8')
    
    # 炙甘草汤（复脉汤）
    # 炙草 桂枝 人参 麻仁 生地 阿胶 麦冬 生姜 大枣
    
    # 桂枝汤
    # 桂枝 白芍 炙草 生姜 大枣
    
    # 桂枝加附子汤 即桂枝汤加附子。
    
    all_prescriptions = {}
    tbd = []  # 桂枝加附子汤 即桂枝汤加附子
    name = None  # name:text
    for line in source_file:
        line = line.strip()
        if (line.endswith("。")):
            line = line[:-1]
        if (line.startswith("<D>") or not line):
            name = None
            continue
        
        if ("即" in line):  # 桂枝加附子汤 即桂枝汤加附子
            # to_file.write(line + "\n")
            tbd.append(line)
            name = None
            continue
        
        if (not name):  # 炙甘草汤（复脉汤）
            name = line.replace("（", " ").replace("）", " ")
        else:
            names = name.split()
            name = None
            for item in names:
                item = item.strip()
                if (not item):
                    continue
                if (not item in all_prescriptions.keys()):
                    all_prescriptions[item] = []
                all_prescriptions[item].append(line)
                to_file.write(item + ": " + line + "\n")
        
        # to_file.write(line + "\n")
        
    to_file.write("//##############\n")
    keys = sorted(all_prescriptions.keys(), reverse=True)
    for item in tbd:
        item = item.replace("（", " ").replace("）", " ")
        index = item.find("即")
        first = item[:index].strip()
        second = item[index:].strip().replace("、", " ")
        for key in keys:
            search = "即" + key
            if (second.startswith(search)):
                temp = ""
                for value in all_prescriptions[key]:
                    temp = value + " |"
                temp = temp.strip("|")
                second = second.replace(search, temp)
        to_file.write(first + ": " + second + "\n")
    source_file.close()
    to_file.close()


def adjust_prescription(source, to):
    source_file = codecs.open(source, 'r', 'utf-8', 'ignore')
    all_prescriptions = {}
    for line in source_file:
        line = line.strip()
        if not line:
            continue
        
        if line.startswith("//"):
            continue
        index = line.find(":")
        name = line[:index].strip()
        value = line[index + 1:].strip()
        
        if (not name in all_prescriptions.keys()):
            all_prescriptions[name]=[]
        all_prescriptions[name].append(value)
    source_file.close()
    
    to_file = codecs.open(to, 'w+', 'utf-8')
    #for name, value in all_prescriptions.items():
    for name in sorted(all_prescriptions.keys()):
        value = all_prescriptions[name]
        for item in value:
            to_file.write(name + ": " + item + "\n")
    to_file.close()


if __name__ == "__main__":
    source_file = os.path.abspath("../DataGenerator/resource/集方.txt")
    to_file = os.path.abspath("../DataGenerator/resource/debug/常用处方_ignore.txt")

    #parse_prescription(source_file, to_file)
    
    final_file = os.path.abspath("../DataGenerator/resource/常用处方.txt")
    adjust_prescription(to_file, final_file)
    print("done")
    pass
