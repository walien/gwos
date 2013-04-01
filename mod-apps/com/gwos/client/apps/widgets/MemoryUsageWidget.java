/*
 * Copyright 2013 - Elian ORIOU
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gwos.client.apps.widgets;

import java.util.Map.Entry;
import java.util.Set;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.charts.PieChart;
import com.gwos.client.domain.impl.StatObject;
import com.gwos.client.domain.impl.User;

public class MemoryUsageWidget {

	public Chart render(StatObject stat) {
		Chart chart = new Chart("resources/chart/open-flash-chart.swf");
		chart.setChartModel(buildChartModel(stat));
		return chart;
	}

	private ChartModel buildChartModel(StatObject stat) {

		Legend lg = new Legend(Position.RIGHT, true);

		final PieChart chart = new PieChart();
		chart.setBorder(1);
		chart.setAlpha(0.3f);
		chart.setAnimate(true);
		chart.setGradientFill(true);
		chart.setTooltip("#label# - #val#");

		// Build data
		Set<Entry<User, Long>> entries = stat.memoryUsages.entrySet();
		double totalSize = stat.fileSystemTotalSpace;
		for (Entry<User, Long> entry : entries) {
			double user = entry.getValue();
			double ratio = (double) (user / totalSize);
			chart.addSlice(ratio * 100, entry.getKey().getUsername() + " ("+entry.getKey().getFirstName()+" "+entry.getKey().getLastName()+")");
		}

		ChartModel model = new ChartModel("Memory Usage");
		model.setBackgroundColour("#ffffff");
		model.setLegend(lg);
		model.addChartConfig(chart);

		return model;
	}
}
