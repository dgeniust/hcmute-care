import '../css/infinitescrolling.css'
const InfiniteScrollingCarousel = () => {

    return (
        
<div class="flex h-full w-full items-center justify-center">
  <div class="relative w-full max-w-screen-lg overflow-hidden">

    <div class="pointer-events-none absolute -top-1 z-10 h-20 w-full bg-gradient-to-b from-white"></div>
    <div class="pointer-events-none absolute -bottom-1 z-10 h-20 w-full bg-gradient-to-t from-white"></div>
    <div class="pointer-events-none absolute -left-1 z-10 h-full w-20 bg-gradient-to-r from-white"></div>
    <div class="pointer-events-none absolute -right-1 z-10 h-full w-20 bg-gradient-to-l from-white"></div>

    <div class="mx-auto grid h-[250px] w-[300px] animate-skew-scroll grid-cols-1 gap-5 sm:w-[600px] sm:grid-cols-2">
      {[
          { title: "Khám chuyên khoa", onClick: null },
          { title: "Hồ sơ sức khỏe", onClick: null },
          { title: "Lịch sử đặt khám", onClick: null },
          { title: "Đặt lịch uống thuốc", onClick: null },
          { title: "Chỉ số BMI, BMR", onClick: () => handlePageChange('bmi') },
          { title: "Lịch sử tiêm chủng", onClick: null },
          ].map((card, index) => (
            <div
              key={index}
              class="flex cursor-pointer items-center space-x-2 rounded-md border border-gray-100 p-5 shadow-md transition-all hover:-translate-y-1 hover:translate-x-1 hover:scale-[1.025] hover:shadow-xl"
              onClick={card.onClick}
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6 flex-none text-violet-600">
                <circle cx="12" cy="12" r="10" />
                <path d="m9 12 2 2 4-4" />
                </svg>
                <h1 className="text-gray-600">{card.title}</h1>
            </div>
        ))}   
        {[
          { title: "Khám chuyên khoa", onClick: null },
          { title: "Hồ sơ sức khỏe", onClick: null },
          { title: "Lịch sử đặt khám", onClick: null },
          { title: "Đặt lịch uống thuốc", onClick: null },
          { title: "Chỉ số BMI, BMR", onClick: () => handlePageChange('bmi') },
          { title: "Lịch sử tiêm chủng", onClick: null },
          ].map((card, index) => (
            <div
              key={index}
              class="flex cursor-pointer items-center space-x-2 rounded-md border border-gray-100 p-5 shadow-md transition-all hover:-translate-y-1 hover:translate-x-1 hover:scale-[1.025] hover:shadow-xl"
              onClick={card.onClick}
            >
              <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="h-6 w-6 flex-none text-violet-600">
                <circle cx="12" cy="12" r="10" />
                <path d="m9 12 2 2 4-4" />
                </svg>
                <h1 className="text-gray-600">{card.title}</h1>
            </div>
        ))}   
    </div>
  </div>
</div>

    )

}
export default InfiniteScrollingCarousel