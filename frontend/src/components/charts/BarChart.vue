<template>
  <div ref="chartRef" class="bar-chart" :style="{ height: height + 'px' }"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: { type: Object, default: () => ({}) },
  height: { type: Number, default: 300 },
  horizontal: { type: Boolean, default: false }
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
  
  const series = (props.data.series || []).map(s => ({
    name: s.name,
    type: 'bar',
    data: s.data
  }))

  const option = {
    tooltip: { trigger: 'axis' },
    legend: { data: props.data.legend || [] },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: props.horizontal 
      ? { type: 'value' }
      : { type: 'category', data: props.data.xAxis || [] },
    yAxis: props.horizontal
      ? { type: 'category', data: props.data.xAxis || [] }
      : { type: 'value' },
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
.bar-chart { width: 100%; }
</style>
