#!/usr/bin/env bash
###
### Set Up Script for Jenkins CI based on Docker
###

set -o nounset
set -o pipefail
set -o errexit
### Uncomment to enable debugging
#set -o verbose
#set -o xtrace

declare -r READLINK="${READLINK:-$(type -p greadlink readlink | head -1)}"
declare -r AWK="${AWK:-$(type -p gawk awk | head -1)}"

### Paths in container need to be prefixed with "/" in order to work in Bash on Windows.
declare -r WIN_ESC="/"
declare -r MY_CMD="$(${READLINK} "${0}")"
declare -r MY_DIR="$(dirname "${MY_CMD}")"
declare -r MY_REALNAME="$(basename "${MY_CMD}")"
### In help texts we might want to show the name the user used to call this script rather than
### its real name.
declare -r MY_NAME="$(basename "${0}")"

declare -r CID_FILE="${MY_DIR}/${MY_REALNAME}.cid"

### To which port to export docker to.
declare -ri ENV_DOCKER_PORT=${DOCKER_PORT:-8080}

function docker_build() {
  cd "${MY_DIR}"
  docker build --tag jenkins/jenkins:joala .
}

function docker_stop() {
  if [ -f "${CID_FILE}" ]; then
    local cid="$(cat "${CID_FILE}")"
    docker stop "${cid}" && docker rm "${cid}" || true
    rm -f "${CID_FILE}"
  fi
}

function docker_setup_secrets() {
  echo "admin" | docker secret create jenkins-user -
  echo "admin" | docker secret create jenkins-pass -
}

function docker_start() {
  docker run \
    --name "joala-ci" \
    --publish ${ENV_DOCKER_PORT}:8080 \
    --cidfile "${CID_FILE}" \
    --detach \
    jenkins/jenkins:joala
}

function finally() {
  echo "Done."
}

function main() {
  trap finally EXIT

  docker_stop
  docker_build
  docker_start
}

main "${@}"
