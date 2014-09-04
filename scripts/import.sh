#!/bin/sh

set -e

CURL="curl --user smaash:password"
URI_BASE="http://localhost:8080/smaash"

# Security labels
$CURL -X PUT -d "1: STAFF, \
2: CONTRACTORS, \
3: FINANCE, \
4: HR, \
5: MARKETING, \
6: PR, \
7: MANAGEMENT, \
8: LONDON, \
9: NEW YORK, \
10: PARIS" "${URI_BASE}/positions"

# Users
$CURL -X PUT -d "HR,LONDON" "${URI_BASE}/p/rich"
$CURL -X PUT -d "PR,NEW YORK" "${URI_BASE}/p/sian"

# Resources
$CURL -X PUT -d "HR" "${URI_BASE}/r/web:images:wansdyke.jpg"
