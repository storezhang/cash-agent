#!/usr/bin/env bash

set -e

# 只在检测到有新的标签的时候才部署
if [ ! -z "$TRAVIS_TAG" ]; then
    echo -e "发现新标签，准备将新版本发布到Docker中央仓库"

    docker build -t storezhang/cash-agent:$TRAVIS_TAG ../
    docker run -d storezhang/cash-agent:$TRAVIS_TAG
    docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD";
    docker push storezhang/cash-agent:$TRAVIS_TAG;
else
    echo -e "没有发现新标签，退出执行"
fi