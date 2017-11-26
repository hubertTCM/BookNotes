# -*- coding: utf-8 -*-
import codecs
import os

def load_alias(source):
    alias = {}
    source_file = codecs.open(source, 'r', 'utf-8', 'ignore')
    for line in source_file:
        line = line.strip()
        if (line.startswith("// TBD")):
            break
        index = line.find(":")
        if (index < 0):
            continue
        name = line[:index].strip()
        value = line[index + 1:].strip()
        alias[name] = value
    return alias

def load_prescriptions(source):
    all_prescriptions = {}
    source_file = codecs.open(source, 'r', 'utf-8', 'ignore')
    for line in source_file:
        line = line.strip()
        if (line.startswith("// TBD")):
            break
        index = line.find(":")
        if (index < 0):
            continue
        name = line[:index].strip()
        value = line[index + 1:].strip()
        if (not name in all_prescriptions.keys()):
            all_prescriptions[name] = []
        all_prescriptions[name].append(value)
    return all_prescriptions

def get_refered_prescriptions(line, prescriptions, alias):
    names = sorted(prescriptions.keys(), reverse=True)
    alias_names = sorted(alias.keys(), reverse=True)
    for name in names:
            index = line.find(name)
            if (index >=0):
                return prescriptions[name]

    for name in alias_names:
        index = line.find(name)
        if (index >=0):
            realname = alias[name]
            return prescriptions[realname]
    
    return []

def add_recipe_content(rootdir, to_directory, prescriptions, alias):
    for subdir, dirs, files in os.walk(rootdir):
        for file in files:
            file_path = subdir + os.sep + file
            
            to_sub_directory = subdir.replace(rootdir, to_directory)
            #print(to_sub_directory)
            if not os.path.exists(to_sub_directory):
                os.makedirs(to_sub_directory)
            to_path = file_path.replace(rootdir, to_directory)
    
            update_detail_file(file_path, to_path, prescriptions, alias)

def update_detail_file(source_file_path, to_file_path, prescriptions, alias):
    read_file = codecs.open(source_file_path, 'r', 'utf-8', 'ignore')
    lines = [item for item in read_file]
    read_file.close()
    
    write_file = codecs.open(to_file_path, 'w', 'utf-8', 'ignore')
    for line in lines:
        line= line.strip()
        write_file.write(line + "\n")
        value = get_refered_prescriptions(line, prescriptions, alias)
        for content in value:
            write_file.write("<format> " + content + "\n")
    write_file.close()

if __name__ == "__main__":
    consilia_directory = os.path.abspath("../DataGenerator/resource/临证指南医案/test")
    to_directory = os.path.abspath("../DataGenerator/resource/临证指南医案/test_ignore")
    prescription_file = os.path.abspath("../DataGenerator/resource/常用处方.txt")
    
    all_prescriptions = load_prescriptions(prescription_file)
    alias_file = os.path.abspath("../DataGenerator/resource/处方缩写.txt")
    alias = load_alias(alias_file)
    add_recipe_content(consilia_directory, to_directory, all_prescriptions, alias)
    print("done")
