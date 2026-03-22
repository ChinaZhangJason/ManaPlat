export const chartColors = {
  default: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc'],
  categorical: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de'],
  warm: ['#ff6b6b', '#ffa502', '#ffd93d', '#6bcb77', '#4d96ff'],
  cool: ['#5f27cd', '#341f97', '#1e3799', '#4a69bd', '#6a89cc'],
  light: ['#dfe6e9', '#b2bec3', '#636e72', '#2d3436', '#0984e3']
}

export const chartTheme = {
  color: chartColors.default,
  backgroundColor: 'transparent',
  textStyle: {
    fontFamily: 'Arial, sans-serif',
    fontSize: 12,
    color: '#666'
  },
  title: {
    textStyle: {
      fontSize: 16,
      fontWeight: 'bold',
      color: '#333'
    }
  },
  legend: {
    textStyle: {
      color: '#666'
    }
  },
  tooltip: {
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: '#e8e8e8',
    textStyle: {
      color: '#333'
    }
  }
}

export const getChartColor = (index, palette = 'default') => {
  const colors = chartColors[palette] || chartColors.default
  return colors[index % colors.length]
}

export const getChartGradient = (color, direction = 'vertical') => {
  return direction === 'vertical' 
    ? { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [
        { offset: 0, color: color },
        { offset: 1, color: 'rgba(255,255,255,0.1)' }
      ]}
    : { type: 'linear', x: 0, y: 0, x2: 1, y2: 0, colorStops: [
        { offset: 0, color: color },
        { offset: 1, color: 'rgba(255,255,255,0.1)' }
      ]}
}

export const defaultLineOption = {
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category', boundaryGap: false },
  yAxis: { type: 'value' }
}

export const defaultBarOption = {
  tooltip: { trigger: 'axis' },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: { type: 'category' },
  yAxis: { type: 'value' }
}

export const defaultPieOption = {
  tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
  legend: { orient: 'vertical', left: 'left' }
}

export const defaultGaugeOption = {
  series: [{
    type: 'gauge',
    startAngle: 180,
    endAngle: 0,
    min: 0,
    max: 100,
    splitNumber: 8
  }]
}
