<template>
  <div ref="chartRef" class="radar-chart" :style="{ height: height + 'px' }"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: { type: Object, default: () => ({}) },
  height: { type: Number, default: 300 }
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
  
  const indicator = (props.data.indicator || []).map(item => ({
    name: item.name,
    max: item.max || 100
  }))

  const series = [{
    type: 'radar',
    data: (props.data.series || []).map(s => ({
      value: s.data,
      name: s.name,
      areaStyle: { opacity: 0.3 }
    }))
  }]

  const option = {
    tooltip: {},
    legend: { data: (props.data.series || []).map(s => s.name) },
    radar: { indicator },
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
.radar-chart { width: 100%; }
</style>
