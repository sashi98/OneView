COMMA_DELIM=','

function validate_current_deployment() {
    if [ ${SUPPORTED_DEPLOYMENTS} == "" ]; then
        error_log "[CONFIGURATION ERROR] 'supported.deployments' property is not configured properly"
        exit 0
    fi
    if [ ${CURRENT_DEPLOYMENT} == "" ]; then
        error_log "[CONFIGURATION ERROR] 'current.deployment' property is not configured properly"
        exit 0
    fi

    local valid=$(string_contains "${CURRENT_DEPLOYMENT}" "${SUPPORTED_DEPLOYMENTS}" $COMMA_DELIM)
    echo "${valid}"
}

function validate_current_deployment_type() {
    if [ ${SUPPORTED_DEPLOYMENTS_TYPES} == "" ]; then
        error_log "[CONFIGURATION ERROR] 'supported.deployments.types' property is not configured properly"
        exit 0
    fi
    if [ ${CURRENT_DEPLOYMENT_TYPE} == "" ]; then
        error_log "[CONFIGURATION ERROR] 'current.deployment.type' property is not configured properly"
        exit 0
    fi

    local valid=$(string_contains "${CURRENT_DEPLOYMENT_TYPE}" "${SUPPORTED_DEPLOYMENTS_TYPES}" $COMMA_DELIM)
    echo "${valid}"
}

function validation() {
    if [ $(validate_current_deployment) == "false" ]; then
        error_log "Current deployment is not valid"
        exit 1
    fi

    if [ $(validate_current_deployment_type) == "false" ]; then
        error_log "Current deployment type is not valid"
        exit 1
    fi

    info_log "Validation is done.."
}