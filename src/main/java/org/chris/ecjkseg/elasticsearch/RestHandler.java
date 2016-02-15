package org.chris.ecjkseg.elasticsearch;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.OK;

import org.chris.ecjkseg.Dictionary;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;

public class RestHandler extends BaseRestHandler {
	private Dictionary dic;
	@Inject
	public RestHandler(Settings settings, RestController controller, Client client,Dictionary dic) {
		super(settings, controller, client);
		this.dic = dic;
		controller.registerHandler(GET, "/_ecjk", this);
	}

	@Override
	protected void handleRequest(RestRequest request, RestChannel channel, Client client) throws Exception {
		String stats = request.param("stats","count");
		String res = "null";
		if(stats.equals("count")){
			res = String.valueOf(this.dic.getWordsCount());
		}
		channel.sendResponse(new BytesRestResponse(OK, res));
	}
}
