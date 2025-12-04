<template>
  <!-- 主内容区 -->
  <div class="mx-auto px-20">
    <!-- 我买到的 -->
    <div class="space-y-2">
      <!-- 数据表格 -->
    <div class="bg-white rounded-lg shadow-sm overflow-hidden">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                个人信息
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                融资详情
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                申请状态
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                联合融资
              </th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                操作
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-gray-200">
            <tr v-for="(item, index) in financingList" :key="index" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ item.realName }}</div>
                <div class="text-sm text-gray-500">{{ item.phone }}</div>
                <div class="text-sm text-gray-500">{{ item.idNum }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ item.money }}万元</div>
                <div class="text-sm text-gray-500">{{ item.repayment }}个月</div>
                <div class="text-sm text-gray-500">{{ item.rate }}%</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="getStatusClass(item.status)">
                  {{ getStatusText(item.status) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span :class="item.combinationName1 ? 'text-emerald-600' : 'text-gray-500'">
                  {{ item.combinationName1 ? '是' : '否' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                <div class="flex space-x-2">
                  <button 
                    @click="confirmDelete(item)"
                    class="text-red-600 hover:text-red-900"
                  >
                    删除
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="financingList.length === 0">
              <td colspan="6" class="px-6 py-10 text-center text-gray-500">
                暂无数据
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
    </div>
    </div>

  </div>
</template>
<script setup>
import { ref, reactive, computed, onMounted } from 'vue';
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { useStore } from "vuex";

const store = useStore();
const router = useRouter();
// 融资列表数据
const financingList = ref([]);
const currentData = ref();
// 初始化加载
onMounted(async() => {
  const res = await selectData({});
  financingList.value = res;
});

// 后端请求数据数据
const selectData = async ({  }) => {
  try {
    //检索URL
    const url = ref(`/finance/selectByName`);

    const response = await apiClient.get(url.value, {
      headers: {
        Authorization: window.localStorage.token
      },
    });
    if (response.flag) {
      return response.data;
    } else {
      ElMessage.error(response.data);
    }
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};


// 格式化手机号
const formatPhone = (phone) => {
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2');
};

// 格式化身份证号
const formatIdCard = (idCard) => {
  return idCard.replace(/(\d{6})\d{8}(\d{4})/, '$1********$2');
};

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    2: '已通过',
    1: '已拒绝',
    0: '处理中'
  };
  return statusMap[status] || status;
};

// 获取状态样式
const getStatusClass = (status) => {
  const baseClass = 'px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full';
  const statusClassMap = {
    2: `${baseClass} bg-green-100 text-green-800`,
    1: `${baseClass} bg-red-100 text-red-800`,
    0: `${baseClass} bg-blue-100 text-blue-800`
  };
  return statusClassMap[status] || `${baseClass} bg-gray-100 text-gray-800`;
};

// 确认删除
const confirmDelete = async(item) => {
  try{
    if(item.status!=0){
      ElMessage.error("贷款已审批，无法删除。");
      return;
    }
  if (confirm("确定要删除商品吗？")) {
  //删除
      const response = await apiClient.delete(`/finance/delete/${item.financeId}`, {
        headers: {
          Authorization: window.localStorage.token,
        }
      });
      if (response.flag == true) {
        ElMessage.success("删除成功。");
      }
      else {
        ElMessage.error("删除失败，请重试。");
      }

      //重新加载贷款数据
      const res = await selectData({});
      financingList.value = res;
  }
  } catch (error) {
    console.error('删除失败，请重试:', error);
  }
};

</script>