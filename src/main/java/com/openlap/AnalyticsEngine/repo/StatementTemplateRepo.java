package com.openlap.AnalyticsEngine.repo;

import com.openlap.AnalyticsEngine.dto.AggItems;

import java.util.List;

public interface StatementTemplateRepo {

	List<AggItems> findDataByCustomAggregate();
}
