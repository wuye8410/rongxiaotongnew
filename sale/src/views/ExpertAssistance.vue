<template>
  <!-- Hero Section -->
  <section class="hero-section relative h-[500px] w-full">
    <!-- 轮播容器 -->
    <div class="carousel-container w-full h-full relative overflow-hidden">
      <!-- 第一页 -->
      <div
        class="carousel-item absolute inset-0 w-full h-full opacity-0 transition-opacity duration-500 bg-[url('../assets/img/banner01.png')] bg-cover bg-center"
        id="carousel-item01"
      >
        <div class="container mx-auto px-6 h-full flex items-center">
          <div class="max-w-2xl">
            <h1 class="text-5xl font-bold text-white mb-6">
              助力农业发展，普惠金融服务
            </h1>
            <div
              class="bg-gradient-to-r from-[#007029] to-transparent p-4 rounded-lg inline-block"
            >
              <p class="text-xl text-white mb-0">
                运用科技手段，提供智能化金融服务，助力农业现代化
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第二页 -->
      <div
        class="carousel-item absolute inset-0 w-full h-full opacity-0 transition-opacity duration-500 bg-[url('../assets/img/banner02.png')] bg-cover bg-center"
        id="carousel-item02"
      >
        <div class="container mx-auto px-6 h-full flex items-center">
          <div class="max-w-2xl">
            <h1 class="text-5xl font-bold text-white mb-6">
              创新金融科技，服务三农发展
            </h1>
            <div
              class="bg-gradient-to-r from-[#007029] to-transparent p-4 rounded-lg inline-block"
            >
              <p class="text-xl text-white mb-0">
                运用科技手段，提供智能化金融服务，助力农业现代化
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 第三页 -->
      <div
        class="carousel-item absolute inset-0 w-full h-full opacity-0 transition-opacity duration-500 bg-[url('../assets/img/banner03.png')] bg-cover bg-center"
        id="carousel-item03"
      >
        <div class="container mx-auto px-6 h-full flex items-center">
          <div class="max-w-2xl">
            <h1 class="text-5xl font-bold text-white mb-6">
              专业农业金融，助力乡村振兴
            </h1>
            <div
              class="bg-gradient-to-r from-[#007029] to-transparent p-4 rounded-lg inline-block"
            >
              <p class="text-xl text-white mb-0">
                提供专业的农业金融解决方案，支持农村经济发展
              </p>
            </div>
          </div>
        </div>
      </div>

      <!-- 轮播指示器 -->
      <div
        class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2"
      >
        <button
          class="carousel-dot w-3 h-3 rounded-full bg-white opacity-50 transition-opacity duration-300"
        ></button>
        <button
          class="carousel-dot w-3 h-3 rounded-full bg-white opacity-50 transition-opacity duration-300"
        ></button>
        <button
          class="carousel-dot w-3 h-3 rounded-full bg-white opacity-50 transition-opacity duration-300"
        ></button>
      </div>
    </div>
  </section>
  <section class="py-12 bg-white border-t-2 border-gray-300">
    <div class="container w-2/3 mx-auto">
      <div class="border-b border-gray-200">
        <div class="container mx-auto px-4">
          <div class="flex items-center justify-between py-4">
            <div class="flex space-x-8">
              <a
                href="#"
                @click.prevent="activeSection = 'knowledge'"
                :class="[
                  activeSection === 'knowledge'
                    ? 'text-green-800 font-medium border-b-2 border-green-800'
                    : 'text-gray-600 hover:text-green-800',
                ]"
                class="pb-4 text-xl"
              >
                农业知识
              </a>
              <a
                href="#"
                @click.prevent="activeSection = 'qa'"
                :class="[
                  activeSection === 'qa'
                    ? 'text-green-800 font-medium border-b-2 border-green-800'
                    : 'text-gray-600 hover:text-green-800',
                ]"
                class="pb-4 text-xl"
              >
                农业问答
              </a>
              <a
                href="#"
                @click.prevent="activeSection = 'experts'"
                :class="[
                  activeSection === 'experts'
                    ? 'text-green-800 font-medium border-b-2 border-green-800'
                    : 'text-gray-600 hover:text-green-800',
                ]"
                class="pb-4 text-xl"
              >
                专家介绍
              </a>
            </div>
          </div>
        </div>
      </div>

      <!-- Main Content -->
      <div class="container mx-auto px-4 py-6">
        <!-- 农业知识 Section -->
        <div v-if="activeSection === 'knowledge'">
          <AssistancePage1 />
        </div>

        <!-- 农业问答 Section -->
        <div v-if="activeSection === 'qa'">
          <AssistancePage2 />
        </div>

        <!-- 专家介绍 Section (Placeholder) -->
        <div v-if="activeSection === 'experts'">
          <AssistancePage3 />
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref } from "vue";
import { onMounted } from "vue";
import { UserIcon } from "lucide-vue-next";
import { apiClient } from "../api/apiService.js";
import { useRoute,useRouter } from "vue-router";
import AssistancePage1 from "../components/AssistancePage1.vue"; // 引入农业知识组件
import AssistancePage2 from "../components/AssistancePage2.vue"; // 引入农业问答组件
import AssistancePage3 from "../components/AssistancePage3.vue"; // 引入专家列表组件
import Pagination from "../components/Pagination.vue";
// 当前激活的部分


const route = useRoute();

const activeSection = ref("knowledge");

// Import images
import title01 from "@/assets/img/title01.jpg";
import banner01 from "@/assets/img/banner01.png";

// Carousel data
const currentSlide = ref(0);
const carouselImages = ref([
  { src: title01, alt: "Green agricultural fields" },
  { src: banner01, alt: "Agriculture banner" },
]);

// Auto-rotate carousel
onMounted(() => {
  const items = document.querySelectorAll(".carousel-item");
  const dots = document.querySelectorAll(".carousel-dot");
  let currentIndex = 0;

  // 显示指定索引的轮播项
  function showSlide(index) {
    items.forEach((item) => (item.style.opacity = "0"));
    dots.forEach((dot) => (dot.style.opacity = "0.5"));

    items[index].style.opacity = "1";
    dots[index].style.opacity = "1";
  }

  // 切换到下一张
  function nextSlide() {
    currentIndex = (currentIndex + 1) % items.length;
    showSlide(currentIndex);
  }

  // 初始显示第一张
  showSlide(0);

  // 设置自动轮播
  setInterval(nextSlide, 2000);

  // 点击指示器切换轮播
  dots.forEach((dot, index) => {
    dot.addEventListener("click", () => {
      currentIndex = index;
      showSlide(currentIndex);
    });
  });

 if(route.query.activeSection){
    activeSection.value = route.query.activeSection;
  }
});
const currentFinSlide = ref(0);
const totalSlides = 4;

const nextSlide = () => {
  if (currentFinSlide.value < totalSlides - 1) {
    currentFinSlide.value++;
  } else {
    currentFinSlide.value = 0;
  }
};

const prevSlide = () => {
  if (currentFinSlide.value > 0) {
    currentFinSlide.value--;
  } else {
    currentFinSlide.value = totalSlides - 1;
  }
};




// // 分页状态
// const pagination = ref({
//   currentPage: 1,
//   pageSize: 10,
//   total: 0,
// });

// // 表格数据
// const articleData = ref([]);

// // 后端请求数据数据
// const selectData = async ({ page }) => {
//   try {
//     const response = await apiClient.get(`/knowledge/${page}`, {
//       headers: {
//         Authorization: window.localStorage.token,
//       },
//     });
//     console.log("请求成功", response.data);
//     return response.data;
//   } catch (error) {
//     console.error("请求失败", error);
//     throw error;
//   }
// };

// // 加载数据
// const loadData = async ({ page }) => {
//   try {
//     const res = await selectData({ page })
//     articleData.value = res.list
//     pagination.value.total = res.total
//   } catch (error) {
//     console.error('数据加载失败:', error)
//   }
// }

// // 更新当前页
// const setCurrentPage = (page) => {
//   pagination.value.currentPage = page;
// };

// // 更新每页条数
// const setPageSize = (size) => {
//   pagination.value.pageSize = size;
// };

// // 初始化加载
// onMounted(() => {
//   loadData({
//     page: pagination.value.currentPage
//   });
// });
</script>
