package com.openlap.AnalyticsEngine.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.openlap.AnalyticsEngine.dto.LoginUser;
import com.openlap.AnalyticsEngine.dto.Request.IndicatorPreviewRequest;
import com.openlap.AnalyticsEngine.dto.Request.QuestionSaveRequest;
import com.openlap.AnalyticsEngine.dto.Response.*;
import com.openlap.AnalyticsEngine.model.OpenLapUser;
import com.openlap.AnalyticsEngine.repo.StatementTemplateRepo;
import com.openlap.AnalyticsEngine.service.AnalyticsEngineService;
import com.openlap.AnalyticsMethods.model.AnalyticsMethods;
import com.openlap.AnalyticsModules.model.AnalyticsGoal;
import com.openlap.Visualizer.model.VisualizationLibrary;
import com.openlap.dataset.OpenLAPColumnConfigData;
import com.openlap.dataset.OpenLAPDataSet;
import com.openlap.dynamicparam.OpenLAPDynamicParam;
import com.openlap.exceptions.OpenLAPDataColumnException;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AnalyticsEngine/")
public class AnalyticsEngineController {

	final AnalyticsEngineService analyticsEngineService;

	@Autowired
	public AnalyticsEngineController(AnalyticsEngineService analyticsEngineService) {
		this.analyticsEngineService = analyticsEngineService;
	}

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	StatementTemplateRepo statementTemplateRepo;

	@GetMapping("/test")
	@ResponseBody
	public Object testMongo() {
		return statementTemplateRepo.findDataByCustomAggregate();
	}

	@PostMapping("/UserRegistration")
	@ResponseBody
	public OpenLapUser UserRegistration(@RequestBody OpenLapUser openLapUser) {
		return analyticsEngineService.UserRegistration(openLapUser);
	}

	@PostMapping("/UserLogin")
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody LoginUser loginUser) throws AuthenticationException {
		return analyticsEngineService.UserLogin(loginUser);
	}

	@GetMapping("/initializeDatabase")
	@ResponseBody
	public String initializeDatabase(HttpServletRequest request) {
		return analyticsEngineService.initializeDatabase(request);
	}

	@GetMapping("/GetIndicatorDataHQL")
	@ResponseBody
	public String GetIndicatorDataHQL(@RequestParam(value = "tid", required = true) String triadID,
																		@RequestParam Map<String, String> allRequestParams,
																		HttpServletRequest request) throws OpenLAPDataColumnException, JSONException, JsonProcessingException {
		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
//		try {
		return analyticsEngineService.executeIndicatorHQL(allRequestParams, baseUrl);
//		} catch (Exception exc) {
//			return exc.getMessage();
//		}
	}


	//	***************************************************************************************************************
	//	ANALYSIS METHODS START
	//	***************************************************************************************************************
	@GetMapping("/GetAnalyticsMethods")
	@ResponseBody
	public List<AnalyticsMethods> GetAllMethods(@RequestParam Map<String, String> allRequestParams,
																							HttpServletRequest request) {
		return analyticsEngineService.getAllAnalyticsMethods(request);
	}

	@GetMapping("/GetAnalyticsMethodInputs")
	@ResponseBody
	public List<OpenLAPColumnConfigData> GetAnalyticsMethodInputs(@RequestParam String id,
																																@RequestParam Map<String, String> allRequestParams,
																																HttpServletRequest request) {
		return analyticsEngineService.getAnalyticsMethodInputs(id, request);
	}

	@GetMapping("/GetAnalyticsMethodOutputs")
	@ResponseBody
	public List<OpenLAPColumnConfigData> GetAnalyticsMethodOutputs(@RequestParam String id,
																																 @RequestParam Map<String, String> allRequestParams,
																																 HttpServletRequest request) {
		return analyticsEngineService.getAnalyticsMethodOutputs(id, request);
	}

	@GetMapping("/GetAnalyticsMethodParams")
	@ResponseBody
	public List<OpenLAPDynamicParam> GetAnalyticsMethodParams(@RequestParam String id,
																														@RequestParam Map<String, String> allRequestParams,
																														HttpServletRequest request) {
		return analyticsEngineService.getAnalyticsMethodDynamicParams(id, request);
	}

	//	***************************************************************************************************************
	//	ANALYSIS METHODS END
	//	***************************************************************************************************************
	//	***************************************************************************************************************
	//	VISUALIZATION METHODS START
	//	***************************************************************************************************************
	@GetMapping("/GetVisualizations")
	@ResponseBody
	public List<VisualizationLibrary> GetAllVisualizations(@RequestParam Map<String, String> allRequestParams,
																												 HttpServletRequest request) {
		return analyticsEngineService.getAllVisualizations(request);
	}

	@GetMapping("/GetVisualizationMethodInputs")
	@ResponseBody
	public List<OpenLAPColumnConfigData> GetVisualizationMethodInputs(@RequestParam String frameworkId,
																																		@RequestParam String methodId,
																																		@RequestParam Map<String, String> allRequestParams,
																																		HttpServletRequest request) {
		return analyticsEngineService.getVisualizationMethodInputs(frameworkId, methodId, request);
	}

	@GetMapping("/GetVisualizationMethods")
	@ResponseBody
	public List<VisualizationLibrary> GetVisualizationMethods(@RequestParam String libraryId,
																														@RequestParam Map<String, String> allRequestParams,
																														HttpServletRequest request) {
		return analyticsEngineService.getVisualizationsMethods(libraryId, request);
	}

	//	***************************************************************************************************************
	//	VISUALIZATION METHODS END
	//	***************************************************************************************************************
	//	***************************************************************************************************************
	//	QUERY FOR GOAL, QUESTION & INDICATORS START
	//	***************************************************************************************************************
	@GetMapping("/GetAllGoals")
	@ResponseBody
	public List<AnalyticsGoal> GetAllGoals(@RequestParam Map<String, String> allRequestParams,
																				 HttpServletRequest request) {
		return analyticsEngineService.getAllGoals(request);
	}

	@GetMapping("/GetActiveGoals")
	@ResponseBody
	public List<AnalyticsGoal> GetActiveGoals(@RequestParam(value = "uid", required = false) String uid,
																						@RequestParam Map<String, String> allRequestParams,
																						HttpServletRequest request) {
		return analyticsEngineService.getActiveGoals(uid, request);
	}

	@GetMapping("/SaveGoal")
	@ResponseBody
	public AnalyticsGoal SaveGoal(@RequestParam String name, @RequestParam String description,
																@RequestParam String author, @RequestParam Map<String, String> allRequestParams,
																HttpServletRequest request) {
		return analyticsEngineService.saveGoal(name, description, author, request);
	}

	@GetMapping("/SetGoalStatus")
	@ResponseBody
	public AnalyticsGoal SetGoalStatus(@RequestParam String goalId, @RequestParam boolean isActive,
																		 @RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
		return analyticsEngineService.setGoalStatus(goalId, isActive, request);
	}

	@GetMapping("/GetQuestions")
	@ResponseBody
	public List<QuestionResponse> GetQuestions(@RequestParam Map<String, String> allRequestParams,
																						 HttpServletRequest request) {
		return analyticsEngineService.getQuestions(request);
	}

	@GetMapping("/GetIndicators")
	@ResponseBody
	public List<IndicatorResponse> GetIndicators(@RequestParam Map<String, String> allRequestParams,
																							 HttpServletRequest request) {
		return analyticsEngineService.getIndicators(request);
	}

	@GetMapping("/ValidateQuestionName")
	@ResponseBody
	public Boolean ValidateQuestionName(@RequestParam String name) {
		return analyticsEngineService.validateQuestionName(name);
	}

	@GetMapping("/GetIndicatorsByQuestionId")
	@ResponseBody
	public List<IndicatorResponse> GetIndicatorsByQuestionId(@RequestParam String questionId,
																													 @RequestParam Map<String, String> allRequestParams,
																													 HttpServletRequest request) {
		return analyticsEngineService.getIndicatorsByQuestionId(questionId);
	}

	@GetMapping("/GetIndicatorsByUserName")
	@ResponseBody
	public List<QuestionIndicatorResponse> GetIndicatorsByUserName(@RequestParam String userName,
																																 @RequestParam Map<String, String> allRequestParams,
																																 HttpServletRequest request) {
		return analyticsEngineService.getIndicatorsByUserName(userName);
	}

	@GetMapping("/ValidateIndicatorName")
	@ResponseBody
	public Boolean ValidateIndicatorName(@RequestParam String name) {
		return analyticsEngineService.validateIndicatorName(name);
	}

	@GetMapping("/GetIndicatorData")
	@ResponseBody
	public String GetIndicatorData(@RequestParam(value = "tid", required = true) String triadID,
																 @RequestParam Map<String, String> allRequestParams,
																 HttpServletRequest request) {
		//String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		String baseUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
		try {
			//return analyticsEngineService.executeIndicator(allRequestParams, baseUrl);
			return "Outdated request call. Please get new indicator request code from OpenLAP.";
		} catch (Exception exc) {
			return exc.getMessage();
		}
	}

	// This is actually triad ID search
	@GetMapping("/GetIndicatorById")
	@ResponseBody
	public IndicatorResponse GetTriadById(@RequestParam String indicatorId,
																				@RequestParam Map<String, String> allRequestParams,
																				HttpServletRequest request) {
		return analyticsEngineService.getTriadById(indicatorId, request);
	}

	@PostMapping("/SaveQuestionAndIndicators")
	@ResponseBody
	public QuestionSaveResponse SaveQuestionAndIndicators(@RequestBody QuestionSaveRequest questionSaveRequest,
																												HttpServletRequest request) {
		//String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		return analyticsEngineService.saveQuestionAndIndicators(questionSaveRequest, baseUrl);
	}

	@GetMapping("/GetIndicatorRequestCode")
	@ResponseBody
	public IndicatorSaveResponse GetIndicatorRequestCode(@RequestParam String indicatorId,
																											 HttpServletRequest request) {
		return analyticsEngineService.getIndicatorRequestCode(indicatorId, request);
	}

	@GetMapping("/GetQuestionRequestCode")
	@ResponseBody
	public QuestionSaveResponse GetQuestionRequestCode(@RequestParam String questionId,
																										 HttpServletRequest request) {
		return analyticsEngineService.getQuestionRequestCode(questionId, request);
	}

	@PostMapping("/GetIndicatorPreview")
	@ResponseBody
	public IndicatorPreviewResponse GetIndicatorPreview(@RequestBody IndicatorPreviewRequest indicatorPreviewRequest,
																											@RequestParam Map<String, String> allRequestParams,
																											HttpServletRequest request) {
		//String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		String baseUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
		return analyticsEngineService.getIndicatorPreview(indicatorPreviewRequest, baseUrl);
	}

	@PostMapping("/GetCompositeIndicatorPreview")
	@ResponseBody
	public IndicatorPreviewResponse GetCompositeIndicatorPreview(@RequestBody IndicatorPreviewRequest previewRequest,
																															 @RequestParam Map<String, String> allRequestParams,
																															 HttpServletRequest request) {
		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		return analyticsEngineService.getCompIndicatorPreview(previewRequest, baseUrl);
	}

	@PostMapping("/GetMLAIIndicatorPreview")
	@ResponseBody
	public Object GetMLAIIndicatorPreview(@RequestBody IndicatorPreviewRequest previewRequest,
																													@RequestParam Map<String, String> allRequestParams,
																													HttpServletRequest request) {
		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		return analyticsEngineService.getMLAIIndicatorPreview(previewRequest, baseUrl);
	}

	//	***************************************************************************************************************
	//	QUERY FOR GOAL, QUESTION & INDICATORS END
	//	***************************************************************************************************************
	//	***************************************************************************************************************
	//	LEARNING RECORD STORE QUERY START
	//	***************************************************************************************************************
	@GetMapping("/getAllActivities")
	public OpenLAPDataSet getAllActivities() throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getAllActivities();
	}

	@GetMapping("/getActivityExtensionId")
	public OpenLAPDataSet getActivityExtensionId(
			@RequestParam String type
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityExtensionId(type);
	}

	@GetMapping("/getKeysbyContextualIdAndActivityType")
	public OpenLAPDataSet getKeysbyContextualIdAndActivityType(
			@RequestParam String extensionId
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getKeysByContextualIdAndActivityType(extensionId);
	}

	@GetMapping("/getActivitiesExtensionContextValues")
	public OpenLAPDataSet getActivitiesExtensionContextValues(
			@RequestParam("extensionId") String extensionId,
			@RequestParam("extensionContextKey") String extensionContextKey
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivitiesExtensionContextValues(extensionId, extensionContextKey);
	}

	//	Fetch the object types from LRS
	@GetMapping("/getAllPlatforms")
	public OpenLAPDataSet getAllPlatforms() throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getAllPlatforms();
	}

	@GetMapping("/getActivityTypes")
	public OpenLAPDataSet getActivityTypes() throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityTypes();
	}

	//  Fetch the object names from the LRS
	@GetMapping("/getActivityTypeNames")
	public OpenLAPDataSet getActivityTypeNames(
			@RequestParam String type
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityTypeNames(type);
	}

	//	Fetch the actions on the activities from the LRS
	@GetMapping("/getAllActions")
	public OpenLAPDataSet getAllActions(
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getAllActions();
	}

//	//	Fetch only the actions on the activity type and platform from the LRS selected by user
//	@GetMapping("/getActions")
//	public OpenLAPDataSet getActions(
//			@RequestParam String platform,
//			@RequestParam String activityType,
//			@RequestParam String activityNameId,
//			@RequestParam String activityName
//	) throws OpenLAPDataColumnException, JSONException {
//		return analyticsEngineService.getActions(platform, activityType, activityNameId, activityName);
//	}

	//  Fetch the extension IDs of object types from LRS
	@GetMapping("/getActivityTypeExtensionId")
	public OpenLAPDataSet getActivityTypeExtensionId(
			@RequestParam String type
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityTypeExtensionId(type);
	}

	@GetMapping("/getActivityTypeExtensionProperties")
	public OpenLAPDataSet getActivityTypeExtensionProperties(
			@RequestParam String type,
			@RequestParam String extensionId
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityTypeExtensionProperties(type, extensionId);
	}

	@GetMapping("/getActivityTypeExtensionPropertyValues")
	public OpenLAPDataSet getActivityTypeExtensionPropertyValues(
			@RequestParam String extensionId,
			@RequestParam String attribute
	) throws OpenLAPDataColumnException, JSONException {
		return analyticsEngineService.getActivityTypeExtensionPropertyValues(extensionId, attribute);
	}
	//	***************************************************************************************************************
	//	LEARNING RECORD STORE QUERY END
	//	***************************************************************************************************************
}
