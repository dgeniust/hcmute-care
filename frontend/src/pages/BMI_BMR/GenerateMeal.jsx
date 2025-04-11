import React, { useState, useEffect } from 'react';
import { Collapse, Card, Statistic, Tag, Empty, Progress, Button, Input, Modal, Descriptions  } from 'antd';
import { ClockCircleOutlined, FireTwoTone, HeartOutlined, StarOutlined, FireOutlined, BookOutlined } from '@ant-design/icons';
import imgBgFood from '../../assets/bg-food-gen.jpg'
const { Panel } = Collapse;
const { Meta } = Card;
import {cleanRecipeData, formatTime} from '../../utils/formatData.js'
const GenerateMeal = ({tdee}) => {

  const meal_api = import.meta.env.VITE_API_MEAL_GENERATION;

  const [data, setData] = useState()
  const fetchMeal = async () => {
    try {
      const response = await fetch(`${meal_api}${tdee}?num_options=1`,
        {
          method: 'POST',
        }
      );
      if (!response.ok) {
        throw new Error(`Lỗi: ${response.status} - ${response.statusText}`);
      }
      const meals = await response.json();
      const cleaned = cleanRecipeData(meals);
      console.log("✅ Dữ liệu nhận được:", cleaned);
      setData(cleaned);
    } catch (error) {
      console.error("❌ Lỗi khi fetch:", error);
    }
  };
  const handleGenerateMeal = () => {
    fetchMeal();
  }
  
  const formatMealName = (name) => {
    const mealNames = {
      "Bua sang": "Bữa Sáng",
      "Bua trua": "Bữa Trưa",
      "Bua toi": "Bữa Tối"
    };
    return mealNames[name] || name;
  };


  // Tính phần trăm macros
  const calculateMacroPercentage = (protein, carbs, fat) => {
    const total = protein * 4 + carbs * 4 + fat * 9; // calories từ mỗi macro
    if (total === 0) return { protein: 0, carbs: 0, fat: 0 };
    
    return {
      protein: Math.round((protein * 4 / total) * 100),
      carbs: Math.round((carbs * 4 / total) * 100),
      fat: Math.round((fat * 9 / total) * 100)
    };
  };

  // Tạo tiêu đề cho mỗi Panel với tổng calories
  const getPanelHeader = (title, totalCalories) => {
    // Tạo màu nền dựa trên calories
    let bgColor = 'bg-green-50';
    let textColor = 'text-green-700';
    
    if (totalCalories > 700) {
      bgColor = 'bg-red-50';
      textColor = 'text-red-700';
    } else if (totalCalories > 500) {
      bgColor = 'bg-yellow-50';
      textColor = 'text-yellow-700';
    }
    
    return (
      <div className={`flex items-center justify-between w-full p-2 rounded-lg ${bgColor}`}>
        <div className="flex items-center">
          <div className={`text-2xl font-bold ${textColor}`}>
            {formatMealName(title)}
          </div>
        </div>
        <div className="flex items-center">
          <FireTwoTone  className={`text-xl mr-2`} twoToneColor="#e74c3c"/>
          <Statistic 
            value={totalCalories} 
            suffix="kcal" 
            valueStyle={{ 
              color: totalCalories > 700 ? '#cf1322' : totalCalories > 500 ? '#d48806' : '#3f8600',
              fontSize: '1.5rem',
              fontWeight: 'bold'
            }}
          />
        </div>
      </div>
    );
  };

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [dataModal, setDataModal] = useState();
  const showModal = (data) => {
    setIsModalOpen(true);
    setDataModal(data);
    console.log(data);
  };
  const handleCancel = () => {
    setIsModalOpen(false);
  };
  // Tạo các panel cho Collapse
  const mealPanels = data ? Object.entries(data).map(([mealName, mealData]) => {
    const totalCalories = mealData[0]?.TotalCalories || 0;
    const recipes = mealData[0]?.Recipes || [];
    return (
      <Panel 
        key={mealName} 
        header={getPanelHeader(mealName, totalCalories)}
        className="mb-6 border-0 shadow-md rounded-xl overflow-hidden"
      >
        {
            recipes.length > 0 ? (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 p-4">
          {recipes.map(recipe => {
            const macros = calculateMacroPercentage(
              recipe.ProteinContent, 
              recipe.CarbohydrateContent, 
              recipe.FatContent
            );
            
            return (
              <Card
                key={recipe.RecipeId}
                hoverable
                className="overflow-hidden rounded-lg transition-all duration-300 transform hover:-translate-y-1 hover:shadow-xl"
                cover={
                  <div className="h-48 overflow-hidden relative">
                    <img 
                      src={recipe.Images === 'h' ? imgBgFood : recipe.Images} 
                      alt={recipe.Name}
                      className="w-full h-full object-cover"
                      // onError={(e) => {
                      //   e.target.src = "/api/placeholder/400/300";
                      // }}
                    />
                    <div className="absolute top-0 right-0 m-2">
                      <Tag color={recipe.Calories > 300 ? "error" : recipe.Calories > 200 ? "warning" : "success"} className="px-3 py-1 text-sm font-bold">
                        <FireTwoTone className="mr-1"/> {recipe.Calories} kcal
                      </Tag>
                    </div>
                    <div className="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black to-transparent p-3">
                      <Tag color="blue" className="opacity-90">{recipe.RecipeCategory}</Tag>
                    </div>
                  </div>
                }
                actions={[
                  <div key="time">
                    <ClockCircleOutlined className="mr-1" /> 
                    {formatTime(recipe.TotalTime || (recipe.PrepTime || recipe.CookTime))}
                  </div>,
                  <div key="reviews">
                    <StarOutlined className="mr-1" /> 
                    {recipe.ReviewCount || "0"} đánh giá
                  </div>,
                  <div key="view" onClick={() => showModal(recipe)}>
                    <HeartOutlined className="mr-1" /> 
                    Chi tiết
                  </div>
                ]}
              >
                  <Meta
                  title={<div className="text-lg font-bold truncate">{recipe.Name}</div>}
                  description={
                    <div className="space-y-4 mt-2">
                      <div className="grid grid-cols-2 gap-2">
                        <div className="flex items-center">
                          <span className="mr-2">Nấu:</span>
                          <Tag color="processing" className="m-0">{formatTime(recipe.CookTime)}</Tag>
                        </div>
                        <div className="flex items-center">
                          <span className="mr-2">Chuẩn bị:</span>
                          <Tag color="processing" className="m-0">{formatTime(recipe.PrepTime)}</Tag>
                        </div>
                      </div>
                      
                      <div>
                        <div className="flex justify-between mb-1">
                          <span className="text-xs">Protein: {recipe.ProteinContent}g</span>
                          <span className="text-xs font-bold">{macros.protein}%</span>
                        </div>
                        <Progress percent={macros.protein} size="small" showInfo={false} status="active" strokeColor="#722ed1" />
                        
                        <div className="flex justify-between mb-1 mt-2">
                          <span className="text-xs">Carbs: {recipe.CarbohydrateContent}g</span>
                          <span className="text-xs font-bold">{macros.carbs}%</span>
                        </div>
                        <Progress percent={macros.carbs} size="small" showInfo={false} status="active" strokeColor="#52c41a" />
                        
                        <div className="flex justify-between mb-1 mt-2">
                          <span className="text-xs">Chất béo: {recipe.FatContent}g</span>
                          <span className="text-xs font-bold">{macros.fat}%</span>
                        </div>
                        <Progress percent={macros.fat} size="small" showInfo={false} status="active" strokeColor="#faad14" />
                      </div>
                    </div>
                  }
                />
              </Card>
            );
          })}
        </div>
            ) : (
                <Empty description="Không có món nào trong bữa này" className="py-10" />
            )
        }
      </Panel>
    );
  }) : [];
  const renderInstructions = () => {
    if (!dataModal?.RecipeInstructions) {
      return (
        <div className="flex items-center justify-center p-8 bg-gray-50 rounded-lg">
          <div className="text-gray-500 italic">No instructions available</div>
        </div>
      );
    }
  
    // Handle the string format from the data
    let instructions = [];
    try {
      // The instructions format appears to be c("step1", "step2", ...)
      const instructionText = dataModal.RecipeInstructions;
      const instructionArray = instructionText
        .replace('c(', '')
        .replace(/\)$/, '')
        .split('", "')
        .map(item => item.replace(/^"|"$/g, ''));
      
      instructions = instructionArray;
    } catch (e) {
      // If parsing fails, try to display as is
      instructions = [dataModal.RecipeInstructions];
    }
  
    return (
      <div className="space-y-4">
        {instructions.map((step, index) => (
          <div key={index} className="flex bg-gray-50 rounded-lg p-4 hover:bg-gray-100 transition-colors">
            <div className="flex-shrink-0 mr-4">
              <div className="flex items-center justify-center w-10 h-10 bg-blue-500 text-white rounded-full font-bold">
                {index + 1}
              </div>
            </div>
            <div className="flex-1">
              <h4 className="font-medium text-gray-800 mb-1">Step {index + 1}</h4>
              <p className="text-gray-700">{step}</p>
            </div>
          </div>
        ))}
      </div>
    );
  };
  const renderIngredients = () => {
    if (!dataModal.RecipeIngredientParts || dataModal.RecipeIngredientParts.length === 0) {
      return (
        <div className="flex items-center justify-center p-8 bg-gray-50 rounded-lg">
          <div className="text-gray-500 italic">No ingredients available</div>
        </div>
      );
    }
    
    // Common units that might be included in the ingredients
    const commonUnits = ['cup', 'cups', 'tbsp', 'tsp', 'oz', 'g', 'kg', 'ml', 'l', 'lb', 'lbs', 'pinch', 'pinches'];
    
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        {dataModal.RecipeIngredientParts.map((ingredient, index) => {
          let quantity = dataModal.RecipeIngredientQuantities && 
                        dataModal.RecipeIngredientQuantities[index] ? 
                        dataModal.RecipeIngredientQuantities[index] : '';
          
          // For display purposes, convert fractions to a nicer format
          // This handles common fractions like 1/2, 1/4, 3/4, etc.
          if (quantity.includes('/')) {
            // Keep it as is - these are typically fractions
          } else if (!isNaN(quantity)) {
            // If it's a number, make sure it shows as a number
            quantity = quantity === '' ? '' : Number(quantity).toString();
          }
          
          // Try to identify if the first word of the ingredient is a unit of measurement
          const words = ingredient.trim().toLowerCase().split(' ');
          const firstWord = words[0];
          const unit = commonUnits.includes(firstWord) ? firstWord : '';
          
          // If the first word is a unit, remove it from the ingredient name for display
          const displayIngredient = unit ? ingredient.substring(ingredient.indexOf(' ')+1) : ingredient;
          
          return (
            <div 
              key={index} 
              className="flex items-center p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors"
            >
              <div className="w-2 h-2 bg-green-500 rounded-full mr-3"></div>
              <div className="flex-1">
                <span className="capitalize">
                  {quantity && (
                    <span className="font-medium text-gray-700 mr-1">
                      {quantity}{unit ? ` ${unit}` : ''}
                    </span>
                  )}
                  <span className="text-gray-800">{displayIngredient}</span>
                </span>
              </div>
            </div>
          );
        })}
      </div>
    );
  };
  const formatTimeDetail = (time) => {
    if (!time) return 'N/A';
    // Extract minutes from PT format (e.g., "PT15M" → "15 min")
    const minutes = time.replace('PT', '').replace('M', '');
    return `${minutes} min`;
  };
  return (
    <div className="w-full h-full mx-auto p-4">
      
      <Button onClick={handleGenerateMeal} style={{width: '100%', height: '35px', color: '#273c75', backgroundColor: 'white', fontWeight: 'bold', fontSize: '15px', border: '1px solid #273c75', marginLeft: '10px'}}>
        Gợi ý bữa ăn
      </Button>
      {
        data ? (
          <>
            <div className="text-center mb-8 p-6 bg-white rounded-xl shadow-md">
              <h1 className="text-3xl font-bold mb-2 text-gray-800">Kế Hoạch Bữa Ăn Hàng Ngày</h1>
              <p className="text-gray-600">Tổng calories: { data ? Object.values(data).reduce((sum, meal) => sum + meal[0]?.TotalCalories || 0, 0) : null} kcal</p>
            </div>
            <Collapse 
              defaultActiveKey={['Bua sang']} 
              expandIconPosition="end"
              className="bg-transparent border-0"
            >
              {mealPanels}
            </Collapse>
          </>
        ) : null
      }
      
      <Modal title={dataModal?.Name || "Công thức chi tiết"} centered open={isModalOpen} onCancel={handleCancel} footer={null} width={800} body={{ maxHeight: '70vh', overflow: 'auto' }} >
        {
          dataModal ? (
            <div className="p-4">
              {/* Recipe timing information */}
              <div className='w-full h-[200px]'>
                <img src={dataModal.Images === 'h' ? imgBgFood : dataModal.Images} alt={dataModal.Name} className="w-full h-full object-cover rounded-lg mb-4" />
              </div>
              <div className="flex justify-between mb-6 bg-gray-50 p-4 rounded-lg">
                <div className="flex flex-col items-center">
                  <ClockCircleOutlined className="text-xl text-blue-500 mb-2" />
                  <div className="text-xs text-gray-500">Prep Time</div>
                  <div className="font-medium">{formatTimeDetail(dataModal.PrepTime)}</div>
                </div>
                <div className="flex flex-col items-center">
                  <FireOutlined className="text-xl text-orange-500 mb-2" />
                  <div className="text-xs text-gray-500">Cook Time</div>
                  <div className="font-medium">{formatTimeDetail(dataModal.CookTime)}</div>
                </div>
                <div className="flex flex-col items-center">
                  <ClockCircleOutlined className="text-xl text-green-500 mb-2" />
                  <div className="text-xs text-gray-500">Total Time</div>
                  <div className="font-medium">{formatTimeDetail(dataModal.TotalTime)}</div>
                </div>
                <div className="flex flex-col items-center">
                  <BookOutlined className="text-xl text-purple-500 mb-2" />
                  <div className="text-xs text-gray-500">Category</div>
                  <div className="font-medium">{dataModal.RecipeCategory || 'N/A'}</div>
                </div>
              </div>

              {/* Nutritional information */}
              <div className="mb-6">
                <h3 className="text-lg font-medium mb-3 border-b pb-2">Nutritional Information</h3>
                <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Calories</div>
                    <div className="font-medium">{dataModal.Calories || 'N/A'} kcal</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Carbohydrates</div>
                    <div className="font-medium">{dataModal.CarbohydrateContent || 'N/A'} g</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Protein</div>
                    <div className="font-medium">{dataModal.ProteinContent || 'N/A'} g</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Fat</div>
                    <div className="font-medium">{dataModal.FatContent || 'N/A'} g</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Fiber</div>
                    <div className="font-medium">{dataModal.FiberContent || 'N/A'} g</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Sugars</div>
                    <div className="font-medium">{dataModal.SugarContent || 'N/A'} g</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Cholesterol</div>
                    <div className="font-medium">{dataModal.CholesterolContent || 'N/A'} mg</div>
                  </div>
                  <div className="bg-gray-50 p-3 rounded">
                    <div className="text-xs text-gray-500">Sodium</div>
                    <div className="font-medium">{dataModal.SodiumContent || 'N/A'} mg</div>
                  </div>
                </div>
              </div>

              {/* Ingredients */}
              <div className="mb-6">
                <h3 className="text-lg font-medium mb-3 border-b pb-2">Ingredients</h3>
                {renderIngredients()}
              </div>

              {/* Instructions */}
              <div className="mb-6">
                <h3 className="text-lg font-medium mb-3 border-b pb-2">Instructions</h3>
                {renderInstructions()}
              </div>

              {/* Recipe ID */}
              <div className="text-right text-xs text-gray-400">
                Recipe ID: {dataModal.RecipeId}
              </div>
            </div>
          ) : null
        }
      </Modal>
    </div>
  );
};

export default GenerateMeal;