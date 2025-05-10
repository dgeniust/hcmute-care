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

export const showData = (rawData, doctors = [], timeSlots = []) => {
  const newData = [];

  if (rawData?.data?.content) {
    rawData.data.content.forEach(schedule => {
      // For GET API responses with scheduleSlots
      if (schedule.scheduleSlots) {
        schedule.scheduleSlots.forEach(slot => {
          newData.push({
            id: `${schedule.id}-${slot.id}`,
            title: schedule.roomDetail?.name || `Room ${schedule.roomId || 'Unknown'}`,
            start: `${schedule.date} ${slot.timeSlot.startTime.slice(0, 5)}`,
            end: `${schedule.date} ${slot.timeSlot.endTime.slice(0, 5)}`,
            description: `${schedule.doctor?.fullName || 'Unknown Doctor'} | ${schedule.doctor?.gender || 'N/A'} | ${slot.bookedSlots}/${schedule.maxSlots}`,
            calendarId: 'work',
            doctorId: schedule.doctor?.id,
            roomId: schedule.roomDetail?.id,
            timeSlotIds: [slot.timeSlot.id],
          });
        });
      } else {
        // For POST API responses with timeSlotIds
        const doctor = doctors.find(d => d.id === schedule.doctorId) || { fullName: 'Unknown Doctor', gender: 'N/A' };
        // Check if timeSlotIds exists and is an array
        if (Array.isArray(schedule.timeSlotIds)) {
          schedule.timeSlotIds.forEach(slotId => {
            const slot = timeSlots.find(s => s.id === slotId);
            if (slot) {
              newData.push({
                id: `${schedule.id}-${slotId}`,
                title: `Room ${schedule.roomId || 'Unknown'}`,
                start: `${schedule.date} ${slot.startTime.slice(0, 5)}`,
                end: `${schedule.date} ${slot.endTime.slice(0, 5)}`,
                description: `${doctor.fullName} | ${doctor.gender} | 0/${schedule.maxSlots}`,
                calendarId: 'work',
                doctorId: schedule.doctorId,
                roomId: schedule.roomId,
                timeSlotIds: [slotId],
              });
            }
          });
        } else {
          console.warn('No valid timeSlotIds found in schedule:', schedule);
        }
      }
    });
  } else {
    console.warn('Invalid rawData structure:', rawData);
  }

  return newData;
};