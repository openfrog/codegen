#!/usr/bin/env bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd ${DIR} && cd ..

./mvnw clean deploy -P release -Darguments="-Dgpg.passphrase=${GPG_PASSPHRASE}"
