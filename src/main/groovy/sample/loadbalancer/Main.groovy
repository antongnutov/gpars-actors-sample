package sample.loadbalancer

class Main {
    static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")

        def balancer = new LoadBalancer(maxWorkers: 4).start()
        
        10.times {
            balancer << Message.WORK
            Thread.sleep(20L)
        }
        
        Thread.sleep(5000L)
    }
}
