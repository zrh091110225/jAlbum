#!/bin/bash

DIR="$(cd "$(dirname "$0")" && pwd )"
cd "${DIR}/dedup"
(java -Xdebug -Xrunjdwp:transport=dt_socket,address=4321,server=y,suspend=n -Dorg.sqlite.lib.path=../jdbcsqlitenative/ -Dorg.sqlite.lib.name=libsqlite.so -jar start.jar >./log/jstdout.log 2>&1  &)
