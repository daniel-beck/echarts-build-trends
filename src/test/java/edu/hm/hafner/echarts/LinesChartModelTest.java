package edu.hm.hafner.echarts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.hm.hafner.echarts.LineSeries;
import edu.hm.hafner.echarts.LineSeries.FilledMode;
import edu.hm.hafner.echarts.LineSeries.StackedMode;
import edu.hm.hafner.echarts.LinesChartModel;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link LinesChartModel}.
 *
 * @author Ullrich Hafner
 */
class LinesChartModelTest {
    private static final String COLOR = "#fff";
    private static final List<String> BUILDS = Arrays.asList("#1", "#2", "#3");
    private static final String ID = "spotbugs";

    @Test
    void shouldBeEmptyWhenCreated() {
        LinesChartModel model = new LinesChartModel();

        assertThat(model.getId()).isEmpty();
        assertThat(model.getSeries()).isEmpty();
        assertThat(model.getDomainAxisLabels()).isEmpty();
    }

    @Test
    void shouldAddLabels() {
        LinesChartModel model = new LinesChartModel(ID);

        model.setDomainAxisLabels(BUILDS);

        assertThat(model.size()).isEqualTo(3);
        assertThat(model.getDomainAxisLabels()).hasSize(3);
        assertThat(model.toString()).isEqualTo(
                "{\"domainAxisLabels\":[\"#1\",\"#2\",\"#3\"],\"buildNumbers\":[],\"series\":[],\"id\":\"spotbugs\"}");
    }

    @Test
    void testGetId() {
        LinesChartModel model = new LinesChartModel(ID);
        assertThat(model.getId()).isEqualTo(ID);

        model.setId("anotherSpotbugs");
        assertThat(model.getId()).isEqualTo("anotherSpotbugs");
    }

    @Test
    void testGetSeries() {
        LinesChartModel model = new LinesChartModel(ID);
        LineSeries series = new LineSeries("TestName", "TestColor", StackedMode.STACKED, FilledMode.FILLED);

        model.addSeries(series);

        assertThat(model.getSeries().get(0)).isEqualTo(series);
    }

    @Test
    void testGetDomainAxisLabels() {
        LinesChartModel modelForSingleXAxisLabelTest = new LinesChartModel(ID);
        modelForSingleXAxisLabelTest.setDomainAxisLabels(Collections.singletonList("a"));
        assertThat(modelForSingleXAxisLabelTest.getDomainAxisLabels())
                .hasSize(1)
                .isEqualTo(Collections.singletonList("a"));

        LinesChartModel modelForXAxisListLabelTest = new LinesChartModel(ID);
        modelForXAxisListLabelTest.setDomainAxisLabels(Arrays.asList("a", "b", "c"));
        assertThat(modelForXAxisListLabelTest.getDomainAxisLabels())
                .hasSize(3)
                .isEqualTo(Arrays.asList("a", "b", "c"));
        modelForXAxisListLabelTest.setDomainAxisLabels(Arrays.asList("d", "e", "f"));
        assertThat(modelForXAxisListLabelTest.getDomainAxisLabels())
                .hasSize(6)
                .isEqualTo(Arrays.asList("a", "b", "c", "d", "e", "f"));
    }

    @Test
    void shouldCreateLineModel() {
        LinesChartModel model = new LinesChartModel(ID);
        List<String> builds = new ArrayList<>();
        List<LineSeries> series = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            builds.add("#" + (i + 1));
        }
        series.add(new LineSeries("High", COLOR, StackedMode.STACKED, FilledMode.FILLED));
        series.add(new LineSeries("Normal", COLOR, StackedMode.STACKED, FilledMode.FILLED));
        series.add(new LineSeries("Low", COLOR, StackedMode.STACKED, FilledMode.FILLED));

        for (LineSeries severity : series) {
            for (int i = 0; i < 5; i++) {
                severity.add(i * 10);
            }
        }

        model.setDomainAxisLabels(builds);
        model.addSeries(series);

        assertThatJson(model).node("domainAxisLabels")
                .isArray().hasSize(5)
                .contains("#1")
                .contains("#2")
                .contains("#3")
                .contains("#4")
                .contains("#5");

        assertThatJson(model).node("series").isArray().hasSize(3);
    }
}
