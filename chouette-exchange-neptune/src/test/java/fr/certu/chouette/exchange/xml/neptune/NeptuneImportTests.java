package fr.certu.chouette.exchange.xml.neptune;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.ValidationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import fr.certu.chouette.common.ChouetteException;
import fr.certu.chouette.model.neptune.Line;
import fr.certu.chouette.plugin.exchange.FormatDescription;
import fr.certu.chouette.plugin.exchange.IImportPlugin;
import fr.certu.chouette.plugin.exchange.ParameterDescription;
import fr.certu.chouette.plugin.exchange.ParameterValue;
import fr.certu.chouette.plugin.exchange.SimpleParameterValue;
import fr.certu.chouette.plugin.report.ReportHolder;

@ContextConfiguration(locations={"classpath:testContext.xml"})
@SuppressWarnings("unchecked")
public class NeptuneImportTests extends AbstractTestNGSpringContextTests
{
	private static final Logger LOGGER = Logger.getLogger(NeptuneImportTests.class);

	private IImportPlugin<Line> importLine = null;
	private String neptuneFile = null;
	private String path="src/test/resources/";

	@Test(groups={"ImportLine"}, description="Get a bean from context")
	public void getBean(){
		importLine = (IImportPlugin<Line>) applicationContext.getBean("NeptuneLineImport") ;
	}

	@Parameters({"neptuneFile"})
	@Test (groups = {"ImportLine"}, description = "Import Plugin should import neptune file",dependsOnMethods={"getBean"})
	public void getNeptuneFile(String neptuneFile){
		this.neptuneFile = neptuneFile;
	}

	@Test (groups = {"ImportLine"}, description = "Import Plugin should import file",dependsOnMethods={"getBean"})
	public void verifyImportLine() throws ChouetteException
	{

		List<ParameterValue> parameters = new ArrayList<ParameterValue>();
		SimpleParameterValue simpleParameterValue = new SimpleParameterValue("xmlFile");
		simpleParameterValue.setFilepathValue(path+"/"+neptuneFile);
		parameters.add(simpleParameterValue);

		ReportHolder report = new ReportHolder();

		List<Line> lines = importLine.doImport(parameters, report);

		Assert.assertNotNull(lines,"lines cant't be null");
		for(Line line : lines){
			System.out.println(line.toString("\t",80));
		}
		
	}

	@Test (groups = {"ImportLine"}, description = "Import Plugin should return format description",dependsOnMethods={"getBean"})
	public void verifyFormatDescription()
	{
		FormatDescription description = importLine.getDescription();
		List<ParameterDescription> params = description.getParameterDescriptions();

		Assert.assertEquals(description.getName(), "XMLNeptuneLine");
		Assert.assertNotNull(params,"params should not be null");
		Assert.assertEquals(params.size(), 2," params size must equal 2");
		LOGGER.info("Description \n "+description.toString());
		System.out.println(description.toString());

	}
	
	/*@Test (groups = {"ImportLine"}, description = "Import Plugin should validate an xml file",dependsOnMethods={"getBean","verifyImportLine"}, 
			expectedExceptions=ValidationException.class)
	public void verifyValidation() throws ChouetteException
	{

		List<ParameterValue> parameters = new ArrayList<ParameterValue>();
		SimpleParameterValue simpleParameterValue = new SimpleParameterValue("xmlFile");
		simpleParameterValue.setFilepathValue(path+"/"+neptuneFile);
		parameters.add(simpleParameterValue);
		SimpleParameterValue simpleParameterValue2 = new SimpleParameterValue("validateXML");
		simpleParameterValue2.setBooleanValue(true);
		parameters.add(simpleParameterValue2);

		ReportHolder report = new ReportHolder();

		List<Line> lines = importLine.doImport(parameters, report);

		Assert.assertNotNull(lines,"lines cant't be null");
		
	}*/
}