#!/usr/bin/python3

# This python module reads a csv file, and filters the columns to those with a specified name in the header row.

import argparse
import csv

## Globals

tableName = "/DataLog/"
colsToDel = []

##
def main():
    global tableName
    
    ## Usage
    parser = argparse.ArgumentParser()
    parser.add_argument('inFile', help='Input file name')
    parser.add_argument('outFile', help='Output file name')
    args = parser.parse_args()

    with open(args.inFile) as csvfile, \
         open(args.outFile, mode='w', newline='') as outfile:
    
        csvreader = csv.reader(csvfile, delimiter=',', quotechar='"')
        csvwriter = csv.writer(outfile, delimiter=',', quotechar='"')

        firstrow = True
        for row in csvreader:

            # read column headers to generate the filter list
            if (firstrow):
                colNum = 0
                print("Columns found:")
                for data in row:
                    if (data.find(tableName) < 0):
                        colsToDel.append(colNum)
                    else:
                        idx=row[colNum].index(tableName) + len(tableName)
                        row[colNum] = row[colNum][idx:]
                        print(">>> {:s}".format(row[colNum]))
                    colNum = colNum + 1
                colsToDel.reverse()             # reverse the filter list, since columns will be deleted in this order!
                firstrow = False
            
            # remove columns in the filter list
            for colNum in colsToDel:
                del row[colNum]
				
            # write the output
            csvwriter.writerow(row)
            

    
##
if __name__ == "__main__":
    main()
    