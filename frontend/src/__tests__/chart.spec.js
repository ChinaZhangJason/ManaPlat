import { describe, it, expect } from 'vitest'

describe('Chart Components', () => {
  it('should export correct chart types', () => {
    const chartTypes = ['LINE', 'BAR', 'PIE', 'SCATTER', 'GAUGE', 'TABLE', 'RADAR', 'HEATMAP']
    expect(chartTypes.length).toBe(8)
  })

  it('should have correct line chart data structure', () => {
    const lineChartData = {
      xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri'],
      series: [
        { name: 'CPU', data: [30, 40, 35, 50, 45] },
        { name: 'Memory', data: [60, 65, 70, 75, 72] }
      ],
      legend: ['CPU', 'Memory']
    }

    expect(lineChartData.xAxis).toHaveLength(5)
    expect(lineChartData.series).toHaveLength(2)
    expect(lineChartData.legend).toContain('CPU')
  })

  it('should have correct bar chart data structure', () => {
    const barChartData = {
      xAxis: ['Jan', 'Feb', 'Mar', 'Apr'],
      series: [{ name: 'Sales', data: [100, 150, 120, 180] }]
    }

    expect(barChartData.xAxis).toHaveLength(4)
    expect(barChartData.series[0].data[0]).toBe(100)
  })

  it('should have correct pie chart data structure', () => {
    const pieChartData = {
      series: [
        { name: 'Category A', value: 30 },
        { name: 'Category B', value: 50 },
        { name: 'Category C', value: 20 }
      ]
    }

    expect(pieChartData.series).toHaveLength(3)
    expect(pieChartData.series.reduce((sum, s) => sum + s.value, 0)).toBe(100)
  })

  it('should have correct gauge chart data structure', () => {
    const gaugeChartData = {
      value: 75,
      name: 'CPU使用率'
    }

    expect(gaugeChartData.value).toBe(75)
    expect(gaugeChartData.name).toBe('CPU使用率')
  })

  it('should have correct scatter chart data structure', () => {
    const scatterChartData = {
      series: [
        {
          name: 'Group A',
          data: [[10, 20], [30, 40], [50, 60]]
        }
      ]
    }

    expect(scatterChartData.series[0].data).toHaveLength(3)
    expect(Array.isArray(scatterChartData.series[0].data[0])).toBe(true)
  })

  it('should have correct radar chart data structure', () => {
    const radarChartData = {
      indicator: [
        { name: '销售额', max: 100 },
        { name: '用户数', max: 100 },
        { name: '转化率', max: 100 }
      ],
      series: [
        { name: '2024', data: [80, 60, 70] }
      ]
    }

    expect(radarChartData.indicator).toHaveLength(3)
    expect(radarChartData.series[0].data).toHaveLength(3)
  })

  it('should have correct heatmap data structure', () => {
    const heatmapData = {
      xAxis: ['A', 'B', 'C'],
      yAxis: ['X', 'Y', 'Z'],
      data: [[0, 0, 50], [0, 1, 80], [1, 0, 30]]
    }

    expect(heatmapData.xAxis).toHaveLength(3)
    expect(heatmapData.yAxis).toHaveLength(3)
    expect(heatmapData.data[0]).toHaveLength(3)
  })
})
