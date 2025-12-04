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

  <!-- Product Cards -->
  <section class="py-12 bg-gray-50">
    <div class="container mx-auto px-4">
      <h2 class="text-3xl font-bold text-center mb-1">金融产品</h2>
      <h5 class="text-l font-bold text-center mb-8 text-gray-400">
        Financial Products
      </h5>

      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <button
          class="absolute -left-4 z-10 bg-gray-100 rounded-full w-10 h-10 flex items-center justify-center shadow-md"
          @click="prevSlide"
        >
          <ChevronLeft class="w-5 h-5 text-gray-600" />
        </button>
        <div
          v-for="(product, index) in bankFour"
          :key="index"
          class="bg-white rounded-lg shadow-sm overflow-hidden"
        >
          <div class="p-4">
            <div class="bg-white rounded-lg shadow-md p-5">
              <div class="flex flex-col items-center mb-4">
                <img :src="product.icon" class="w-46 h-12 mb-1" />
                <div class="text-sm text-gray-600 text-xl">
                  {{product.name}}
                </div>
              </div>
              <div class="text-center mb-4">
                <div class="text-l text-gray-400 mb-1">最高可申请额度</div>
                <div class="text-3xl font-bold text-green-800">
                 {{product.money}}
                </div>
              </div>
              <div class="text-center mb-4">
                <div class="text-l text-gray-400">在线申请</div>
                <div class="text-l text-gray-400">期限长 还款方式灵活</div>
              </div>
              <button
                class="w-full py-2 bg-green-800 text-white rounded text-sm hover:bg-green-900 transition-colors" @click="finanProDetail(product)"
              >
                我要贷款
              </button>
            </div>
          </div>
        </div>
        <button
          class="absolute -right-4 z-10 bg-gray-100 rounded-full w-10 h-10 flex items-center justify-center shadow-md"
          @click="nextSlide"
        >
          <ChevronRight class="w-5 h-5 text-gray-600" />
        </button>
      </div>
      <div class="flex justify-center gap-5 mt-6">
        <button
          class="px-6 py-2 bg-green-800 text-white rounded-full text-sm hover:bg-green-900 transition-colors flex items-center gap-2" @click="aimatch()"
        >
          <img src="../assets/img/icon_match.png" class="w-4 h-4" />
          智能匹配
        </button>
        <button
          class="px-6 py-2 bg-white text-gray-800 border border-gray-200 rounded-full text-sm hover:bg-gray-50 transition-colors" @click="finanProMore()"
        >
          查看更多
        </button>
      </div>
    </div>
  </section>
  <section>
    <div class="container mx-auto px-4">
      <!-- Specialists Section -->
      <div class="mb-16">
        <h2 class="text-3xl font-bold text-center mb-1">专家介绍</h2>
        <h5 class="text-l font-bold text-center mb-8 text-gray-400">
          Expert Introduction
        </h5>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-10 mb-8">
          <div
            v-for="(specialist, index) in expertFive"
            :key="index"
            class="rounded-lg overflow-hidden shadow-md border border-[rgba(0,112,41,0.3)] hover:bg-green-700 hover:text-white transition-colors duration-300"
          >
            <div class="h-32 w-full flex items-center justify-center relative">
              <div
                class="absolute bottom-0 left-1/2 transform -translate-x-1/2 w-32 h-16 bg-[#007029] rounded-t-full"
                style="z-index: 1"
              ></div>
              <img
                :src="$store.state.imgShowRoad + '/file/avatar/' + specialist.avatar"
                class="h-32 w-32 object-cover relative"
                style="z-index: 2"
              />
            </div>
            <div class="p-4">
              <div class="flex gap-2 mb-4">
                <div
                  class="text-center font-bold text-base py-1 px-2 rounded bg-green-800 text-white flex-1"
                  :class="index === 0 ? 'bg-green-900' : ''"
                  @click="openConsultDialog(specialist)"
                >
                  直接咨询
                </div>
                <div
                  class="text-center font-bold text-base py-1 px-2 rounded bg-green-800 text-white flex-1"
                  :class="index === 0 ? 'bg-green-900' : ''"
                  @click="openReserveDialog(specialist)"
                >
                  线下预约
                </div>
              </div>
              <div class="text-xs">
                <div class="mb-1 flex">
                  <span :class="index === 0 ? 'text-gray-300' : 'text-gray-400'"
                    >专家姓名:</span
                  >
                  <span class="font-medium ml-1">{{ specialist.realName }}</span>
                </div>
                <div class="mb-1 flex">
                  <span :class="index === 0 ? 'text-gray-300' : 'text-gray-400'"
                    >专业:</span
                  >
                  <span class="font-medium ml-1">{{
                    specialist.profession
                  }}</span>
                </div>
                <div class="mb-1 flex">
                  <span :class="index === 0 ? 'text-gray-300' : 'text-gray-400'"
                    >单位:</span
                  >
                  <span class="font-medium ml-1">{{ specialist.belong }}</span>
                </div>
                <div class="mb-1 flex">
                  <span :class="index === 0 ? 'text-gray-300' : 'text-gray-400'"
                    >联系电话:</span
                  >
                  <span class="font-medium ml-1">{{ specialist.phone }}</span>
                </div>
                <div class="mb-1 flex">
                  <span :class="index === 0 ? 'text-gray-300' : 'text-gray-400'"
                    >职称:</span
                  >
                  <span class="font-medium ml-1">{{ specialist.position }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="flex justify-center">
          <button
            class="px-6 py-2 bg-white text-gray-800 border border-gray-200 rounded-full text-sm hover:bg-gray-50 transition-colors" @click="expertAssis('experts')" 
          >
            查看更多
          </button>
        </div>
      </div>
    </div>
  </section>

  <section>
    <div class="container mx-auto px-4">
      <!-- Specialists Section -->
      <div class="mb-16">
        <div class="flex flex-col md:flex-row">
          <!-- Left side - Title and Image -->
          <div class="relative w-full max-w-md mx-auto p-2">
            <!-- Base green card with text -->
            <div class="bg-green-800 text-white p-6 w-3/5 h-64 relative z-10">
              <h2 class="text-xl font-bold mb-1">专家问答</h2>
              <p class="text-xs text-gray-200">Expert Q&A</p>
            </div>

            <!-- Overlapping soybeans image with button -->
            <div class="absolute top-12 right-0 w-3/5 z-20">
              <div class="relative">
                <!-- Soybeans image -->
                <img
                  src="../assets/img/huangdou.png"
                  alt="Soybeans"
                  class="w-54 h-64 object-cover"
                />

                <!-- Green button at bottom -->
                <div
                  class="absolute w-3/5 bottom-10 left-10 right-8 bg-green-800 text-white py-2 text-center" @click="expertAssis('qa')"
                >
                  <span class="text-sm">查看更多</span>
                </div>
              </div>
            </div>
          </div>

          <!-- Right side - FAQ Items -->
          <div class="w-full md:w-3/4 md:pl-2">
            <div v-for="(faq, index) in quesAnsThree" :key="index" class="mb-8">
              <div class="flex items-start">
                <div class="mr-6">
                  <div class="text-4xl font-bold text-green-700">Q&A</div>
                  <div class="text-xs text-gray-500">专家：{{ faq.expertName }}</div>
                </div>
                <div>
                  <h3 class="text-lg font-bold text-green-700 mb-2">
                    {{ faq.question }}
                  </h3>
                  <p class="text-sm text-gray-600 leading-relaxed">
                    {{ faq.answer }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="flex justify-center">
          <button
            class="px-6 py-2 bg-white text-gray-800 border border-gray-200 rounded-full text-sm hover:bg-gray-50 transition-colors" @click="expertAssis('qa')"
          >
            查看更多
          </button>
        </div>
      </div>
    </div>
  </section>

  <section>
    <div class="container mx-auto px-4">
      <!-- Specialists Section -->
      <div class="mb-16 mx-auto">
        <h2 class="text-3xl font-bold text-center mb-1">农业知识</h2>
        <h5 class="text-l font-bold text-center mb-8 text-gray-400">
          Agricultural Knowledge
        </h5>

        <!-- First Article -->
        <div class="flex flex-col md:flex-row items-start mb-12">
          <div
            class="w-full md:w-1/5 mb-4 md:mb-0 flex justify-center items-center"
          >
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + pic01"
              alt="Tea leaves"
              class="rounded-md w-64 h-74"
            />
          </div>
          <div class="w-full md:w-4/5 md:px-8 flex flex-col h-[250px]">
            <div class="flex-grow">
              <h3
                class="text-lg font-bold mb-4 border-l-4 border-green-700 pl-3"
              >
                {{knowledgeTwo[0].title}}
              </h3>
              <p class="text-l text-gray-600 leading-relaxed">
                {{knowledgeTwo[0].content}}
              </p>
            </div>
            <button
              class="bg-green-700 text-white py-2 px-6 rounded-md text-sm hover:bg-green-800 transition-colors self-start" @click="articleDetail(knowledgeTwo[0])"
            >
              查看详情
            </button>
          </div>
        </div>

        <!-- Second Article -->
        <div class="flex flex-col md:flex-row-reverse items-start">
          <div
            class="w-full md:w-1/5 mb-4 md:mb-0 flex justify-center items-center"
          >
            <img
              :src="$store.state.imgShowRoad + '/file/order/' + pic02"
              alt="Tea leaves"
              class="rounded-md w-64 h-74"
            />
          </div>
          <div class="w-full md:w-4/5 md:px-8 flex flex-col h-[250px]">
            <div class="flex-grow">
              <h3
                class="text-lg font-bold mb-4 border-l-4 border-green-700 pl-3"
              >
                {{knowledgeTwo[1]?.title}}
              </h3>
              <p class="text-l text-gray-600 leading-relaxed mb-6">
                {{knowledgeTwo[1]?.content}}
              </p>
            </div>
            <button
              class="bg-green-700 text-white py-2 px-6 rounded-md text-sm hover:bg-green-800 transition-colors self-start" @click="articleDetail(knowledgeTwo[1])"
            >
              查看详情
            </button>
          </div>
        </div>
        <div class="flex justify-center">
          <button
            class="px-6 py-2 bg-white text-gray-800 border border-gray-200 rounded-full text-sm hover:bg-gray-50 transition-colors" @click="expertAssis('knowledge')" 
          >
            查看更多
          </button>
        </div>
      </div>
    </div>
  </section>
  <section>
    <div class="container mx-auto px-4">
      <!-- Specialists Section -->
      <div class="mb-16 mx-auto">
        <h2 class="text-3xl font-bold text-center mb-1">助农电商</h2>
        <h5 class="text-l font-bold text-center mb-8 text-gray-400">
          Agricultural E-commerce
        </h5>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-5 gap-6 mb-8">
          <!-- Product Cards (using v-for) -->
          <div
            v-for="(product, index) in goodsFive"
            :key="index"
            class="border border-gray-200 rounded-md overflow-hidden shadow-sm hover:shadow-md transition-shadow"
            @click="goodsDetail(product)"
          >
            <div class="w-full">
              <img
                :src="$store.state.imgShowRoad + '/file/order/' + product.picture"
                class="w-full h-64 object-cover"
              />
            </div>
            <div class="p-4">
              <p class="text-m text-gray-700 mb-2 line-clamp-2 h-5">
                {{ product.title }}
              </p>
              <p class="text-sm text-gray-700 mb-2 line-clamp-2 h-10">
                {{ product.content }}
              </p>
              <p class="text-red-500 font-medium">¥{{ product.price }}</p>
            </div>
          </div>
        </div>
        <div class="flex justify-center">
          <button
            class="px-6 py-2 bg-white text-gray-800 border border-gray-200 rounded-full text-sm hover:bg-gray-50 transition-colors" @click="proMore()"
          >
            查看更多
          </button>
        </div>
      </div>
    </div>
  </section>
  <ExpertConsultDialog ref="consultDialog" :expertName="expertName"/>
  <ExpertReserveDialog ref="reserveDialog" :expertName="expertName"/>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { UserIcon } from "lucide-vue-next";
import { defineComponent } from "vue";
import { apiClient } from "../api/apiService.js";
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useStore } from "vuex";
import ExpertConsultDialog from "../components/ExpertConsultDialog.vue";
import ExpertReserveDialog from "../components/ExpertReserveDialog.vue";
const store = useStore();
defineComponent({
  name: "HomePage",
});

const consultDialog = ref();
const expertName = ref('');
const openConsultDialog = (expert) => {
  if(!window.localStorage.token){
    ElMessage.error('您未登录，请先登录');
    return;
  }
  expertName.value=expert.userName;
  consultDialog.value.open();
};

const reserveDialog = ref();
const openReserveDialog = (expert) => {
  if(!window.localStorage.token){
    ElMessage.error('您未登录，请先登录');
    return;
  }
  expertName.value=expert.userName;
  reserveDialog.value.open();
};

// Import images
import title01 from "@/assets/img/title01.jpg";
import banner01 from "@/assets/img/banner01.png";
import bank01 from "@/assets/img/zgnyyh.png";
import bank02 from "@/assets/img/zggsyh.png";
import bank03 from "@/assets/img/zgjsyh.png";
import bank04 from "@/assets/img/zgnyyh.png";
import bank05 from "@/assets/img/rzyh.png";
import bank06 from "@/assets/img/zgnyyh.png";
import bank07 from "@/assets/img/zgnyyh.png";
import bank08 from "@/assets/img/zgnyyh.png";
import bank09 from "@/assets/img/zgnyyh.png";
import bank10 from "@/assets/img/zgnyyh.png";
import bank11 from "@/assets/img/zgnyyh.png";

const router = useRouter();

// Carousel data
const currentSlide = ref(0);
const carouselImages = ref([
  { src: title01, alt: "Green agricultural fields" },
  { src: banner01, alt: "Agriculture banner" },
]);

let bankFour = ref([0]);
let expertFive = ref([0]);
let quesAnsThree = ref([0]);
let knowledgeTwo = ref([0]);
const pic01 = ref();
const pic02 = ref();

let goodsFive = ref([0]);

// Auto-rotate carousel
onMounted(async () => {
  const items = document.querySelectorAll(".carousel-item");
  const dots = document.querySelectorAll(".carousel-dot");
  let currentIndex = 0;
  const imgArray = ref([
    {
      id: "1001",
      icon: bank01,
    },
    {
      id: "1002",
      icon: bank02,
    },
    {
      id: "1003",
      icon: bank03,
    },
    {
      id: "1004",
      icon: bank04,
    },
    {
      id: "1005",
      icon: bank05,
    },
    {
      id: "1006",
      icon: bank06,
    },
    {
      id: "1007",
      icon: bank07,
    },
    {
      id: "1008",
      icon: bank08,
    },
    {
      id: "1009",
      icon: bank09,
    },
    {
      id: "1010",
      icon: bank10,
    },
    {
      id: "1011",
      icon: bank11,
    },
  ]);

//编辑展示的银行产品信息
  const bankdata = await selectBank();
 
  const tempFirstFour = bankdata.slice(0, 4);
  let bankMsg = '';

  tempFirstFour.forEach((item) => {
    imgArray.value.map((item1) => {
      if (item.bankId == item1.id) {
        item['icon']=item1.icon;
      }
    });
    bankMsg = item['introduce'].split('，')[0].replace(/"/g, '');
    item['name'] = bankMsg;
  });

  bankFour.value = tempFirstFour;

//编辑展示的专家信息
  const expertdata = await selectExpert();
  const tempExpertFive = expertdata.slice(0, 5);
  expertFive.value = tempExpertFive;

//编辑展示的问答信息
  const questiondata = await selectQuesAns();
  const tempquesAnsThree = questiondata.slice(0, 3);
  quesAnsThree.value = tempquesAnsThree;

//编辑展示的农业知识信息
  const knowledgedata = await selectKnowledge();
  const tempquesKnowledge = knowledgedata.slice(0, 2);
  knowledgeTwo.value = tempquesKnowledge;
  pic01.value = tempquesKnowledge[0].picPath;
  pic02.value = tempquesKnowledge[1].picPath;

//编辑展示的商品信息
  const goodsdata = await selectGoods();
  const tempgoodsdata = goodsdata.slice(0, 5);
  goodsFive.value = tempgoodsdata;

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

// 银行查询接口
const selectBank = async () => {
  try {
    const response = await apiClient.get("/finance/selectbank", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 专家查询接口
const selectExpert = async () => {
  try {
    const response = await apiClient.get("/question/findExpertUser/1", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data.list;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 问答查询接口
const selectQuesAns = async () => {
  try {
    const response = await apiClient.get("/question/findAllQues/1", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data.list;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 农业知识查询接口
const selectKnowledge = async () => {
  try {
    const response = await apiClient.get("/knowledge/1", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data.list;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};

// 商品查询接口
const selectGoods = async () => {
  try {
    const response = await apiClient.get("/order/goods/1", {
      headers: {
        Authorization: window.localStorage.token,
      },
    });
    console.log("请求成功", response.data);
    return response.data.list;
  } catch (error) {
    console.error("请求失败", error);
    throw error;
  }
};
//点击金融产品更多，跳转
const finanProMore = () => {
  router.push(`/financinglist`).catch((err) => err);
};
//点击某金融产品，跳转
const finanProDetail = (item)=> {
  router.push(`/financing?bankId=${item.bankId}&bankicon=${item.icon}&bankname=${item.name}&bankintro=${item.introduce}&bankmoney=${item.money}&bankrate=${item.rate}&bankrepay=${item.repayment}`)
};

const aimatch = ()=> {
  if(!window.localStorage.token){
    ElMessage.error('您未登录，请先登录');
  } else {
    router.push(`/aimatch`)
  }
};

//点击农业知识、专家、问答，跳转
const expertAssis = (item)=> {
  router.push(`/expertassist?activeSection=${item}`)
};

//点击某知识，跳转至知识详情
const articleDetail = (item)=> {
  router.push(`/AgriKnlg?knowledgeId=${item.knowledgeId}&picPath=${item.picPath}&title=${item.title}&content=${item.content}&ownName=${item.ownName}&updateTime=${item.updateTime}`)
};

//点击产品更多，跳转
const proMore = () => {
  router.push(`/agripro`).catch((err) => err);
};
//点击产品详情，跳转
const goodsDetail = (item)=> {
  router.push(`/goodsDetail?orderId=${item.orderId}&title=${item.title}&price=${item.price}&content=${item.content}&picture=${item.picture}&updateTime=${item.updateTime}`)

};

</script>
