package features.filters.experimental.tightlyCoupled

import framework.ReposeValveTest
import org.rackspace.deproxy.Deproxy
import org.rackspace.deproxy.MessageChain
import org.rackspace.deproxy.Response

class TightlyCoupledFilterTest extends ReposeValveTest {

    /**
     * This test proves that a custom filter, even though it's tightly coupled to repose
     * can modify the response body
     * @return
     */
    def "Proving that a custom filter (although tightly coupled) does in fact work" () {
        setup:

        def params = properties.defaultTemplateParams
        repose.configurationProvider.applyConfigs("common", params)
        repose.configurationProvider.applyConfigs("features/filters/experimental/tightlycoupled", params)

        deproxy = new Deproxy()
        deproxy.addEndpoint(properties.targetPort)
        def started = true
        repose.start([waitOnJmxAfterStarting: false])

        waitUntilReadyToServiceRequests("200", false, true)

        when:
        MessageChain mc = null
        mc = deproxy.makeRequest(
                [
                        method: 'GET',
                        url:reposeEndpoint + "/get",
                        defaultHandler: {
                            new Response(200, null, null, "This should be the body")
                        }
                ])


        then:
        mc.receivedResponse.code == '200'
        mc.receivedResponse.body.contains("<extra> Added by TestFilter, should also see the rest of the content </extra>")
        println(mc.receivedResponse.body)

        cleanup:
        if(started)
            repose.stop()
        if(deproxy != null)
            deproxy.shutdown()

    }
}
