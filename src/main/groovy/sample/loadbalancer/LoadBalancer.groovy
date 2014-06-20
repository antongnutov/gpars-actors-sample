package sample.loadbalancer

import groovyx.gpars.actor.DefaultActor
import groovyx.gpars.group.DefaultPGroup
import groovyx.gpars.group.PGroup

/**
 * Load balancer actor (stateful)
 */
class LoadBalancer extends DefaultActor implements Loggable {
    private def tasks = new PriorityQueue<Message>()
    private workers = 0
    private PGroup group

    int maxWorkers

    void afterStart() {
        group = new DefaultPGroup(maxWorkers)
    }

    @Override
    protected void act() {
        log.info("Starting Load Balancer with 'maxWorkers' = {}", maxWorkers)
        loop {
            react { message ->
                log.debug("Message received: '{}'", message)
                switch (message) {
                    case Message.WORK:
                        tasks << message
                        if (workers < maxWorkers) {
                            log.debug('Creating new worker ...')
                            workers++
                            new Worker(balancer: this, parallelGroup: group).start()
                        } else {
                            log.debug('All workers are busy')
                        }
                        break
                    case Message.READY:
                        if (tasks.isEmpty()) {
                            log.debug('There are no tasks, removing idle worker ...')
                            workers--
                            reply Message.EXIT
                        } else {
                            reply tasks.poll()
                        }
                        break
                    default: log.error("Unsupported message: {}", message)
                }
            }
        }
    }
}
