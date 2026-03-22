import BaseChart from './BaseChart.vue'
import LineChart from './LineChart.vue'
import BarChart from './BarChart.vue'
import PieChart from './PieChart.vue'
import ScatterChart from './ScatterChart.vue'
import GaugeChart from './GaugeChart.vue'
import TableChart from './TableChart.vue'
import RadarChart from './RadarChart.vue'
import HeatmapChart from './HeatmapChart.vue'

export const chartComponents = {
  LINE: LineChart,
  BAR: BarChart,
  PIE: PieChart,
  SCATTER: ScatterChart,
  GAUGE: GaugeChart,
  TABLE: TableChart,
  RADAR: RadarChart,
  HEATMAP: HeatmapChart
}

export const getChartComponent = (type) => {
  return chartComponents[type] || LineChart
}

export {
  BaseChart,
  LineChart,
  BarChart,
  PieChart,
  ScatterChart,
  GaugeChart,
  TableChart,
  RadarChart,
  HeatmapChart
}
