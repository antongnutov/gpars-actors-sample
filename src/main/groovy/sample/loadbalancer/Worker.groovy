package sample.loadbalancer

import groovyx.gpars.actor.StaticDispatchActor

/**
 * Stateless worker actor
 */
class Worker extends StaticDispatchActor<Message> implements Loggable {

    private LoadBalancer balancer

    void afterStart() {
        balancer << Message.READY
    }

    void onMessage(Message m) {
        switch (m) {
            case Message.WORK:
                log.info("Working...")
                Thread.sleep(50L)
                reply Message.READY
                break
            case Message.EXIT:
                log.debug("Exit received, terminating...")
                terminate()
                break
        }
    }
}