package sample.loadbalancer

import org.slf4j.Logger
import org.slf4j.LoggerFactory

trait Loggable {
    Logger log = LoggerFactory.getLogger(getClass())
}