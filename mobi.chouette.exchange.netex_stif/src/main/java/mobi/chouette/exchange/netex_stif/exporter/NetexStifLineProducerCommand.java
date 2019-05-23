package mobi.chouette.exchange.netex_stif.exporter;

import java.io.IOException;
import java.sql.Date;

import javax.naming.InitialContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Constant;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.exchange.netex_stif.NetexStifConstant;
import mobi.chouette.exchange.netex_stif.exporter.producer.NetexStifOffreProducer;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_STATE;
import mobi.chouette.exchange.report.ActionReporter.OBJECT_TYPE;
import mobi.chouette.exchange.report.IO_TYPE;
import mobi.chouette.exchange.report.ObjectReport;
import mobi.chouette.model.LineLite;
import mobi.chouette.model.util.NamingUtil;
import mobi.chouette.model.util.Referential;

@Log4j
public class NetexStifLineProducerCommand implements Command {
	public static final String COMMAND = "NetexStifLineProducerCommand";

	@Override
	public boolean execute(Context context) throws Exception {

		boolean result = Constant.ERROR;
		Monitor monitor = MonitorFactory.start(COMMAND);

		try {

			Long lineId = (Long) context.get(Constant.LINE_ID);
			Referential r = (Referential) context.get(Constant.REFERENTIAL);
			LineLite line = r.findLine(lineId);
			/** If the current line is desactivated, the netex exporter should not export it**/
			if (line.isDeactivated()) {
				return Constant.SUCCESS;
			}
			
			r.setCurrentLine(line);
			log.info("procesing line " + NamingUtil.getName(line));
			NetexStifExportParameters configuration = (NetexStifExportParameters) context.get(Constant.CONFIGURATION);

			ExportableData collection = (ExportableData) context.get(Constant.EXPORTABLE_DATA);
			collection.clearLine();

			Date startDate = null;
			if (configuration.getStartDate() != null) {
				startDate = new Date(configuration.getStartDate().getTime());
			}

			Date endDate = null;
			if (configuration.getEndDate() != null) {
				endDate = new Date(configuration.getEndDate().getTime());
			}

			log.info("global validity period before collect" + collection.getGlobalValidityPeriod());

			NetexStifDataCollector collector = new NetexStifDataCollector();
			collector.collect(context, collection, r, startDate, endDate);
			log.info("global validity period after collect" + collection.getGlobalValidityPeriod());
			if (context.containsKey(NetexStifConstant.OPERATOR_OBJECT_ID)) {

				ObjectReport companyReport = (ObjectReport) context.get(NetexStifConstant.SHARED_REPORT);
				companyReport.addStatTypeToObject(OBJECT_TYPE.LINE, 1);
				companyReport.addStatTypeToObject(OBJECT_TYPE.ROUTE, collection.getRoutes().size());
				companyReport.addStatTypeToObject(OBJECT_TYPE.JOURNEY_PATTERN, collection.getJourneyPatterns().size());
				companyReport.addStatTypeToObject(OBJECT_TYPE.VEHICLE_JOURNEY, collection.getVehicleJourneys().size());
			}

			else {
				ObjectReport lineReport = new ObjectReport(line.getObjectId(), OBJECT_TYPE.LINE,
						NamingUtil.getName(line), OBJECT_STATE.OK, IO_TYPE.INPUT);
				context.put(NetexStifConstant.SHARED_REPORT, lineReport);
				lineReport.setStatTypeToObject(OBJECT_TYPE.JOURNEY_PATTERN, collection.getJourneyPatterns().size());
				lineReport.setStatTypeToObject(OBJECT_TYPE.ROUTE, collection.getRoutes().size());
				lineReport.setStatTypeToObject(OBJECT_TYPE.VEHICLE_JOURNEY, collection.getVehicleJourneys().size());
			}
			NetexStifOffreProducer producer = new NetexStifOffreProducer();
			producer.produce(context);
			result = Constant.SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
		}
		return result;
	}

	public static class DefaultCommandFactory extends CommandFactory {

		@Override
		protected Command create(InitialContext context) throws IOException {
			Command result = new NetexStifLineProducerCommand();
			return result;
		}
	}

	static {
		CommandFactory.register(NetexStifLineProducerCommand.class.getName(), new DefaultCommandFactory());
	}

}
