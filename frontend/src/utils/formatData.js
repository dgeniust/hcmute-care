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
export const cleanRecipeData = (data) => {
  if (!data || typeof data !== 'object') return {};

  // Handle each meal in the data
  const cleanedData = {};
  Object.entries(data).forEach(([mealName, mealArray]) => {
    cleanedData[mealName] = mealArray.map(meal => ({
      TotalCalories: meal.TotalCalories || 0,
      Recipes: meal.Recipes ? meal.Recipes.map(recipe => ({
        RecipeId: recipe.RecipeId || null,
        Name: recipe.Name || '',
        Images: recipe.Images || 'h', // Fallback to 'h' as seen in GenerateMeal
        Calories: recipe.Calories || 0,
        ProteinContent: recipe.ProteinContent || 0,
        CarbohydrateContent: recipe.CarbohydrateContent || 0,
        FatContent: recipe.FatContent || 0,
        FiberContent: recipe.FiberContent || 0,
        SugarContent: recipe.SugarContent || 0,
        CholesterolContent: recipe.CholesterolContent || 0,
        SodiumContent: recipe.SodiumContent || 0,
        PrepTime: recipe.PrepTime || null,
        CookTime: recipe.CookTime || null,
        TotalTime: recipe.TotalTime || null,
        RecipeCategory: recipe.RecipeCategory || '',
        ReviewCount: recipe.ReviewCount || 0,
        RecipeInstructions: cleanArrayField(recipe.RecipeInstructions),
        RecipeIngredientParts: cleanArrayField(recipe.RecipeIngredientParts),
        RecipeIngredientQuantities: cleanArrayField(recipe.RecipeIngredientQuantities),
      })) : [],
    }));
  });

  return cleanedData;
};
// Helper function to clean array fields
const cleanArrayField = (field) => {
  if (!field) return [];
  if (Array.isArray(field)) return field.map(item => item || '');
  if (typeof field !== 'string') return [field || ''];

  // Handle strings that look like arrays (e.g., "[NA, NA]")
  if (field.startsWith('[') && field.endsWith(']')) {
    try {
      // Attempt to parse as JSON
      return JSON.parse(field).map(item => item || '');
    } catch (e) {
      // If parsing fails, manually clean the string
      return field
        .slice(1, -1) // Remove [ and ]
        .split(',')
        .map(item => {
          const trimmed = item.trim();
          // Convert "NA" or invalid values to empty string or null
          return trimmed === 'NA' || trimmed === '' ? '' : trimmed.replace(/^"|"$/g, '');
        });
    }
  }

  return [field];
};
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