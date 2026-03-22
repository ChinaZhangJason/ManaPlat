<template>
  <div ref="chartRef" class="gauge-chart" :style="{ height: height + 'px' }"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: { type: Object, default: () => ({}) },
  height: { type: Number, default: 300 },
  min: { type: Number, default: 0 },
  max: { type: Number, default: 100 }
})

const chartRef = ref(null)
let chart = null

const initChart = () => {
  if (!chartRef.value) return
  chart = echarts.init(chartRef.value)
  updateChart()
}

const updateChart = () => {
  if (!chart) return
  
  const value = props.data.value || 0
  const series = [{
    type: 'gauge',
    startAngle: 180,
    endAngle: 0,
    min: props.min,
    max: props.max,
    splitNumber: 8,
    axisLine: { lineStyle: { width: 6, color: [[1, '#409eff']] } },
    pointer: { icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z', length: '12%', width: 10 },
    axisTick: { length: 12, lineStyle: { color: 'auto', width: 2 } },
    splitLine: { length: 20, lineStyle: { color: 'auto', width: 5 } },
    axisLabel: { color: '#464646', fontSize: 12, distance: -60 },
    title: { offsetCenter: [0, '-10%'], fontSize: 16 },
    detail: { fontSize: 30, offsetCenter: [0, '0%'], valueAnimation: true, formatter: '{value}%', color: 'inherit' },
    data: [{ value: value, name: props.data.name || '' }]
  }]

  const option = {
    series
  }
  
  chart.setOption(option)
}

watch(() => props.data, updateChart, { deep: true })

onMounted(() => {
  initChart()
  window.addEventListener('resize', () => chart?.resize())
})

onUnmounted(() => {
  chart?.dispose()
})
</script>

<style scoped>
.gauge-chart { width: 100%; }
</style>
