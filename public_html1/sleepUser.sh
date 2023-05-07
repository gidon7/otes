#!/bin/sh
logFileName="sleep_$(date +%Y%m%d).log"
curl -so $logFileName "http://lms.malgn.co.kr/sysop/user/sleep_insert.jsp?after=log&mode=sleep&site=all"
mv $logFileName "/home/lms/public_html/data/log/"