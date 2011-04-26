package fr.certu.chouette.validation.test;

import java.util.ArrayList;
import java.util.List;

import fr.certu.chouette.model.neptune.RestrictionConstraint;
import fr.certu.chouette.model.neptune.StopArea;
import fr.certu.chouette.model.neptune.type.ChouetteAreaEnum;
import fr.certu.chouette.plugin.report.Report;
import fr.certu.chouette.plugin.report.ReportItem;
import fr.certu.chouette.plugin.validation.IValidationPlugin;
import fr.certu.chouette.plugin.validation.ValidationClassReportItem;
import fr.certu.chouette.plugin.validation.ValidationParameters;
import fr.certu.chouette.plugin.validation.ValidationStepDescription;
import fr.certu.chouette.validation.report.DetailReportItem;
import fr.certu.chouette.validation.report.SheetReportItem;

/**
 * 
 * @author mamadou keira
 *
 */
public class ValidationStopArea implements IValidationPlugin<StopArea>{

	private ValidationStepDescription validationStepDescription;

	public void init(){
		validationStepDescription = new ValidationStepDescription("Test2.3", ValidationClassReportItem.CLASS.TWO.ordinal());
	}

	@Override
	public ValidationStepDescription getDescription() {
		return validationStepDescription;
	}


	@Override
	public List<ValidationClassReportItem> doValidate(List<StopArea> beans,ValidationParameters parameters) {
		return validate(beans);
	}
	/**
	 * The test 2.3.1
	 * @param stopAreas
	 * @return
	 */
	private List<ValidationClassReportItem> validate(List<StopArea> stopAreas) {
		ValidationClassReportItem category2 = new ValidationClassReportItem(ValidationClassReportItem.CLASS.TWO);
		ReportItem sheet2_3 = new SheetReportItem("Test2_Sheet3",3);
		ReportItem sheet2_12 = new SheetReportItem("Test2_Sheet12",12);
		ReportItem sheet2_13 = new SheetReportItem("Test2_Sheet13",13);

		SheetReportItem report2_3 = new SheetReportItem("Test2_Sheet3_Step1", 1);
		SheetReportItem report2_12 = new SheetReportItem("Test2_Sheet12_Step1", 1);
		SheetReportItem report2_13 = new SheetReportItem("Test2_Sheet13_Step1", 1);

		List<ValidationClassReportItem> result = new ArrayList<ValidationClassReportItem>();
		for(StopArea stopArea :stopAreas){
			List<String> containedStopIds = stopArea.getContainedStopIds(); 
			//Test 2.3.1
			if(containedStopIds != null && !containedStopIds.isEmpty()){
				ChouetteAreaEnum areaType = stopArea.getAreaType();

				if(areaType.equals(ChouetteAreaEnum.BOARDINGPOSITION) || 
						areaType.equals(ChouetteAreaEnum.QUAY)){
					List<String> stopPointIds = StopArea.extractObjectIds(stopArea.getContainedStopPoints()) ;

					if(stopPointIds != null){
						if(!containedStopIds.containsAll(stopPointIds)){
							ReportItem detailReportItem = new DetailReportItem("Test2_Sheet3_Step1_error", Report.STATE.ERROR);
							report2_3.addItem(detailReportItem);
						}else {
							report2_3.updateStatus(Report.STATE.OK);	
						}
					}
				}else {
					List<String> containedAreas = StopArea.extractObjectIds(stopArea.getContainedStopAreas());
					if(containedAreas != null){
						if(!containedStopIds.containsAll(containedAreas)){
							ReportItem detailReportItem =new DetailReportItem("Test2_Sheet3_Step1_error",Report.STATE.ERROR);
							report2_3.addItem(detailReportItem);
						}else {
							report2_3.updateStatus(Report.STATE.OK);	
						}
					}
				}
			}
			List<RestrictionConstraint> constraints = stopArea.getRestrictionConstraints();
			if(constraints != null){
				for (RestrictionConstraint restrictionConstraint : constraints) {
					//Test 2.12.1
					if(restrictionConstraint.getStopAreas() == null){
						ReportItem detailReportItem =new DetailReportItem("Test2_Sheet12_Step1_error",Report.STATE.ERROR);
						report2_12.addItem(detailReportItem);	
					}else {
						if(!StopArea.extractObjectIds(restrictionConstraint.getStopAreas()).contains(stopArea.getObjectId())){
							ReportItem detailReportItem =new DetailReportItem("Test2_Sheet12_Step1_error",Report.STATE.ERROR);
							report2_12.addItem(detailReportItem);	
						}else{
							report2_12.updateStatus(Report.STATE.OK);
						}
					}
					
					//Test 2.13
					if(restrictionConstraint.getLineIdShortCut() != null){
						if(restrictionConstraint.getLine() == null){
							ReportItem detailReportItem =new DetailReportItem("Test2_Sheet13_Step1_error",Report.STATE.ERROR);
							report2_13.addItem(detailReportItem);
						}else {
							report2_13.updateStatus(Report.STATE.OK);	
						}	
					}
				}
			}
		}

		report2_3.computeDetailItemCount();
		report2_12.computeDetailItemCount();
		report2_13.computeDetailItemCount();

		sheet2_3.addItem(report2_3);
		sheet2_12.addItem(report2_12);
		sheet2_13.addItem(report2_13);

		category2.addItem(sheet2_3);
		category2.addItem(sheet2_12);
		category2.addItem(sheet2_13);

		result.add(category2);

		return result;
	}
}