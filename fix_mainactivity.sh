#!/usr/bin/env bash
set -e

FILE="app/src/main/java/com/jagatjit/npuagent/MainActivity.kt"

# Ensure HttpServerService import exists
grep -q 'HttpServerService' "$FILE" || \
sed -i 's/^package com.jagatjit.npuagent$/package com.jagatjit.npuagent

import com.jagatjit.npuagent.HttpServerService/' "$FILE"

# TODO: adjust this sed to match the real line 14 if different
# Example: make lambda parameter type explicit
# sed -i "s/HttpServerService { it ->/HttpServerService { it: Any ->/" "$FILE"
