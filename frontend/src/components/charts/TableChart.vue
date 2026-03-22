<template>
  <div class="table-chart">
    <el-table 
      :data="tableData" 
      border 
      stripe 
      :height="height"
      :default-sort="{ prop: 'sort', order: 'ascending' }"
    >
      <el-table-column v-if="showIndex" type="index" width="60" label="序号" />
      <el-table-column 
        v-for="col in columns" 
        :key="col.prop" 
        :prop="col.prop" 
        :label="col.label"
        :width="col.width"
        :formatter="col.formatter"
        :sortable="col.sortable"
        show-overflow-tooltip
      />
    </el-table>
    <div v-if="pagination && total > 0" class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  data: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
  height: { type: Number, default: 400 },
  showIndex: { type: Boolean, default: true },
  pagination: { type: Boolean, default: false },
  total: { type: Number, default: 0 },
  pageSize: { type: Number, default: 10 }
})

const emit = defineEmits(['page-change'])

const currentPage = ref(1)

const tableData = computed(() => {
  if (!props.pagination) return props.data
  const start = (currentPage.value - 1) * props.pageSize
  return props.data.slice(start, start + props.pageSize)
})

const handlePageChange = (page) => {
  emit('page-change', page)
}
</script>

<style scoped>
.table-chart { width: 100%; }
.pagination { margin-top: 15px; display: flex; justify-content: flex-end; }
</style>
