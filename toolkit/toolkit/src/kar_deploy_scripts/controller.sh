#!/bin/bash
PWD_DIR=$(pwd)

source ./../lib/property_reader.sh "${PWD_DIR}/controller.properties" "${PWD_DIR}/deployment_vars.properties"

#Convention Tip1: USER CONSTANTS NAME IN CAPITAL LETTER.

SUPPORTED_DEPLOYMENTS=$(get_property "supported.deployments")
SUPPORTED_DEPLOYMENTS_TYPES=$(get_property "supported.deployments.types")
CURRENT_DEPLOYMENT=$(get_property "current.deployment")
CURRENT_DEPLOYMENT_TYPE=$(get_property "current.deployment.type")

PROJECT_NAME_KEY="${CURRENT_DEPLOYMENT}.${CURRENT_DEPLOYMENT_TYPE}"
FEATURE_NAME=$(get_property "${PROJECT_NAME_KEY}.feature.name")
REPOS_NAME=$(get_property "${PROJECT_NAME_KEY}.repos.name")
CFG_FILE_NAME=$(get_property "${PROJECT_NAME_KEY}.cfg.file.name")
LASTRUN_FILE_NAME=$(get_property "${PROJECT_NAME_KEY}.last.run.entry.file.name")
BLUEPRINT_FILE_NAME=$(get_property "${PROJECT_NAME_KEY}.blueprint.file.name")

INSTALLABLE_REPOS=$(get_property "${PROJECT_NAME_KEY}.orderwise.installable.repos")


