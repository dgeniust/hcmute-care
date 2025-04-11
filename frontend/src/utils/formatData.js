export const parseRArray = (rString) => {
    if(!rString || rString === "character(0)") return [];
    try {
      return JSON.parse(
        rString.replace(/^c\(/, "[")
        .replace(/\)$/, "]")
        .replace(/\"/g, '"')
        .replace(/\\\"/g, '"')
      );
    }
    catch (error) {
      console.error("❌ Lỗi khi phân tích chuỗi:", error);
      return [];
    }
}
export const cleanRecipeData = (rawData) => {
    const newData = {};
    Object.entries(rawData).forEach(([mealName, mealDetails]) => {
      newData[mealName] = mealDetails.map(meal => {
        const cleanedRecipes = meal.Recipes.map(recipe => ({
          ...recipe,
          RecipeIngredientParts: parseRArray(recipe.RecipeIngredientParts),
          RecipeIngredientQuantities: parseRArray(recipe.RecipeIngredientQuantities),
          Images: parseRArray(recipe.Images)?.[0] || recipe.Images,
        }));
        return {
          ...meal,
          Recipes: cleanedRecipes,
        };
      })
    })
    return newData;
}
export const formatTime = (time) => {
    if (!time) return "N/A";
    
    // Lấy số giờ và phút từ chuỗi PT
    const hourMatch = time.match(/(\d+)H/);
    const minuteMatch = time.match(/(\d+)M/);
    
    const hours = hourMatch ? parseInt(hourMatch[1]) : 0;
    const minutes = minuteMatch ? parseInt(minuteMatch[1]) : 0;
    
    if (hours > 0) {
      return `${hours} giờ ${minutes > 0 ? minutes + ' phút' : ''}`;
    } else {
      return `${minutes} phút`;
    }
};

export const showData = (rawData) => {
  const newData = [];

  if (rawData?.data?.content) {
    rawData.data.content.forEach(schedule => {
      newData.push({
        id: schedule.id.toString(), // Đảm bảo kiểu string
        title: schedule.roomName,
        start: `${schedule.date} ${schedule.startTime.slice(0, 5)}`,
        end: `${schedule.date} ${schedule.endTime.slice(0, 5)}`,
        description: `${schedule.doctorName} | ${schedule.doctorGender} | ${schedule.bookedSlots}/${schedule.maxSlots}`,
        calendarId: 'work'
      });
    });
  }

  return newData;
};