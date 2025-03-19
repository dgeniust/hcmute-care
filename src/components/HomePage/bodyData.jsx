const infoBodyData = [
  {
    x: 100,
    y: 50,
    placement: "left",
    label: "sterno - maftoid",
    name: "Cơ ức đòn chũm",
    image: "https://ik.imagekit.io/dgeniust/Sternocleidomastoid.jpg?updatedAt=1742350772053",
    content:
    "Cơ ức đòn chũm (sternocleidomastoid muscle) là cặp cơ dài ở phía bên vùng cổ, với mỗi cơ có 2 đầu. Đúng như tên gọi, cơ gắn vào xương ức, xương đòn và mỏm chũm xương thái dương. Cơ thuộc lớp nông, nằm rất sát bề mặt da. Nên có thể dễ dàng nhìn thấy và sờ được cơ. Có thể bắt được động mạch cảnh ở 1/3 giữa bờ trước của cơ.",
    function: "Giúp xoay, nghiêng và gập cổ.",
    sick: ["Căng cơ cổ", "Đau cổ"],
    exercise: [
      {
        exName: "Nghiêng cổ sang hai bên",
        exIMG: "https://ik.imagekit.io/dgeniust/coUc1.jpg?updatedAt=1742350763262", // Thêm ảnh nếu có
        equipment: "Không cần thiết bị",
        perform: "Nghiêng đầu sang phải, giữ 5 giây, rồi sang trái, giữ 5 giây. Lặp lại 10 lần.",
        rep: "10 lần",
        rest: "Không cần nghỉ",
      },
      {
        exName: "Xoay cổ",
        exIMG: "https://ik.imagekit.io/dgeniust/exUc2.png?updatedAt=1742350767960",
        equipment: "Không cần thiết bị",
        perform: "Xoay đầu theo chiều kim đồng hồ 5 vòng, rồi ngược chiều kim đồng hồ 5 vòng.",
        rep: "5 vòng mỗi chiều",
        rest: "Không cần nghỉ",
      },
      // Thêm các bài tập khác
    ],
  },
  {
    x: 490,
    y: -10,
    placement: "right",
    label: "trapezius",
    name: "Cơ thang",
    image: "https://ik.imagekit.io/dgeniust/Trapezius_Gray409.png?updatedAt=1742350772229",
    content:
      "Cơ thang (tiếng Latinh: musculus trapezius) ở người là một cơ lớn và mỏng trải dài dọc theo phần trên của cột sống. Cơ thang có nguyên ủy là đường gáy trên, ụ chẩm ngoài, và các mỏm gai của đốt sống từ đốt sống cổ I đến đốt sống ngực XII, và bám tận tại mỏm cùng vai và gai vai. Chức năng của cơ thang là di chuyển xương vai và hỗ trợ cánh tay. Cơ thang được chia thành ba vùng theo chức năng: vùng trên có chức năng hỗ trợ trọng lượng của cánh tay; vùng giữa có chức năng kéo hai xương vai đến gần nhau; và vùng dưới có chức năng xoay và hạ xương vai.",
      function : 'Yếu tố quan trọng trong việc duy trì hình dáng và thẩm mỹ của cơ thể, mà còn là cơ bản trong việc thực hiện nhiều hoạt động hàng ngày. Cơ này giúp điều khiển và hỗ trợ chuyển động của cổ và vai, từ đó ảnh hưởng đến khả năng thực hiện các hoạt động như nâng, kéo và đẩy. Đồng thời, cơ thang cũng giúp bảo vệ các cấu trúc quan trọng ở vùng cổ và lưng trên như dây thần kinh và mạch máu, khỏi những chấn thương có thể xảy ra do va đập hoặc sử dụng sai tư thế.',
    sick: ['Chấn thương cơ thang', 'Căng thẳng cơ thang', 'Hội chứng cổ vai gáy' ],
    exercise : [
        {
            exName:'Nhún vai với tạ đòn',
            exIMG: "https://ik.imagekit.io/dgeniust/exThang1.jpg?updatedAt=1742350767565",
            equipment: 'Tạ đòn',
            weight: '65% mức tạ tối đa có thể nâng được.',
            perform :'Đứng thẳng với chân hẹp hơn vai, nhún vai kéo tạ lên cao hơn vị trí nghỉ. Giữ tư thế này trong 1 - 2 giây rồi lặp lại. Hít vào khi hạ xuống thở ra khi nhún vai kéo tạ lên.',
            rep: '6 - 8 lần x 4 hiệp',
            rest: 'từ 30 đến 60 giây giữa các hiệp',
        },
        {
            exName:'Kéo tạ đòn lên ngang cổ',
            exIMG: "https://ik.imagekit.io/dgeniust/exThang2.jpg?updatedAt=1742350768001",
            equipment: 'Tạ đòn',
            weight: '40% mức tạ tối đa có thể nâng được.',
            perform :'Cầm tạ đòn với tay hẹp, đứng thẳng. Kéo tạ từ vị trí thấp đến ngang cổ sau đó từ từ hạ xuống. Hít vào khi hạ xuống và thở ra khi kéo lên',
            rep: '8 - 10 lần x 4 hiệp',
            rest: 'từ 30 đến 60 giây giữa các hiệp',
        },
        {
            exName:'Nhún cầu vai với tạ đơn',
            exIMG: "https://ik.imagekit.io/dgeniust/exThang3.jpg?updatedAt=1742350768268",
            equipment: 'Tạ đơn',
            weight: '50% mức tạ tối đa có thể nâng được.',
            perform :'Đứng thẳng với chân hẹp hơn vai, nhún vai kéo tạ lên cao hơn vị trí nghỉ. Giữ tư thế này trong 1 - 2 giây rồi lặp lại. Hít vào khi hạ xuống thở ra khi nhún vai kéo tạ lên.',
            rep: '8 - 10 lần x 4 hiệp',
            rest: 'từ 30 đến 60 giây giữa các hiệp',
        }
    ]
  },
  {
    x: 100,
    y: 350,
    placement: "left",
    label: "rectus abdominis",
    name: "Cơ thẳng bụng",
    image: "https://ik.imagekit.io/dgeniust/rectus_abdominis.jpg?updatedAt=1742350771523",
    content:
      "Cơ thẳng bụng (hay cơ thẳng to bụng, tiếng Anh: rectus abdominis muscle), là cơ nằm dọc thành trước của bụng người và bụng của một số động vật có vú khác. Đây là hai cơ song song, ở giữa được ngăn cách bởi một dải gân mô liên kết được gọi là đường trắng. Cơ kéo dài từ khớp mu, mào xương mu và củ mu, đến mỏm mũi kiếm và sụn xương sườn từ V đến VII. Đầu gần là các mào xương mu và khớp mu, đầu xa là các sụn sườn của xương sườn 5-7 và mỏm mũi kiếm của xương ức.",
      function: "Gập bụng, hỗ trợ hô hấp, bảo vệ nội tạng.",
    sick: ["Đau bụng", "Căng cơ bụng"],
    exercise: [
      {
        exName: "Gập bụng cơ bản",
        exIMG: "https://ik.imagekit.io/dgeniust/exBung1.jpg?updatedAt=1742350763340", // Thêm ảnh nếu có
        equipment: "Không cần thiết bị",
        perform: "Nằm ngửa, co gối, gập bụng nâng vai lên. Lặp lại 15 lần.",
        rep: "15 lần",
        rest: "30 giây",
      },
      {
        exName: "Gập bụng chéo",
        exIMG: "https://ik.imagekit.io/dgeniust/exBung2.jpg?updatedAt=1742350763294",
        equipment: "Không cần thiết bị",
        perform: "Nằm ngửa, co gối, gập bụng xoay người sang phải, rồi sang trái. Lặp lại 10 lần mỗi bên.",
        rep: "10 lần mỗi bên",
        rest: "30 giây",
      },
      {
        exName: "Gập bụng ngược",
        exIMG: "https://ik.imagekit.io/dgeniust/exBung3.png?updatedAt=1742350763343",
        equipment: "Không cần thiết bị",
        perform: "Nằm ngửa, co gối, nâng hông lên khỏi mặt thảm, giữ lưng thẳng tự nhiên, thở ra và giữ nguyên tư thế trong 1s, cảm nhận cơ bụng và mông căng lên. Lặp lại 20 lần mỗi bên.",
        rep: "20 lần mỗi bên",
        rest: "30 giây",
      },
    ],
  },
  {
    x: 500,
    y: 350,
    placement: "right",
    label: "serratus anterior",
    name: "Cơ răng trước",
    image: "https://ik.imagekit.io/dgeniust/serratus.jpg?updatedAt=1742350771648",
    content:
      "Cơ răng trước (hay cơ bánh răng trước, tiếng Anh: Serratus anterior muscle, tiếng Pháp: Le muscle dentelé antérieur) là cơ có nguyên ủy từ mặt ngoài của 8 đến 9 xương sườn trên, nằm giữa mặt trong xương vai và các xương sườn. Động tác của cơ là kéo xương vai ra trước, xoay và giữa nó áp vào thành ngực. Về từ nguyên, serrare có nghĩa là răng cưa (hình dạng cơ), anterior có nghĩa là phía trước của cơ thể.",
      function: "Kéo xương vai ra trước, xoay và giữ xương vai áp vào thành ngực.",
    sick: ["Đau vai", "Khó cử động vai"],
    exercise: [
      {
        exName: "Chống đẩy có biến thể",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoTrc1.jpg?updatedAt=1742350767526", // Thêm ảnh nếu có
        equipment: "Không cần thiết bị",
        perform: "Chống đẩy, khi đẩy lên, đẩy vai ra xa nhau. Lặp lại 10 lần.",
        rep: "10 lần",
        rest: "30 giây",
      },
      {
        exName: "Đấm thẳng",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoTrc2.jpg?updatedAt=1742350767833",
        equipment: "Không cần thiết bị",
        perform: "Đứng thẳng, đấm thẳng tay về phía trước, xoay vai khi đấm. Lặp lại 15 lần mỗi tay.",
        rep: "15 lần mỗi tay",
        rest: "30 giây",
      },
      // Thêm các bài tập khác
    ],
  },
  {
    x: 420,
    y: 520,
    placement: "top",
    label: "external oblique",
    name: "Cơ ngoại chéo",
    image: "https://ik.imagekit.io/dgeniust/external.jpg?updatedAt=1742350767480",
    content:
      "External oblique: Nhóm cơ này nằm xung quanh cơ rectus abdominis. Chúng giúp ổn định cơ thể nếu bị xoay hoặc vặn.",
    function: "Giúp xoay thân, nghiêng thân sang một bên và gập thân. Tham gia vào quá trình thở ra mạnh và duy trì áp lực ổ bụng.",
    sick: ["Đau sườn", "Căng cơ liên sườn", "Đau lưng dưới"],
    exercise: [
      {
        exName: "Nghiêng người với tạ tay",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo1.jpg?updatedAt=1742350763281", // Thêm ảnh nếu có
        equipment: "Tạ tay",
        perform: "Đứng thẳng, mỗi tay cầm một quả tạ. Nghiêng người sang phải, giữ 2 giây, rồi nghiêng người sang trái, giữ 2 giây. Lặp lại 12 lần mỗi bên.",
        rep: "12 lần mỗi bên",
        rest: "30 giây",
      },
      {
        exName: "Vặn người kiểu Nga (Russian Twists)",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo2.jpg?updatedAt=1742350763254",
        equipment: "Không cần thiết bị (có thể dùng tạ hoặc bóng)",
        perform: "Ngồi trên sàn, đầu gối hơi cong, chân có thể đặt trên sàn hoặc nhấc lên. Giữ thẳng lưng và vặn thân sang trái, rồi sang phải. Lặp lại 15 lần mỗi bên.",
        rep: "15 lần mỗi bên",
        rest: "30 giây",
      },
      {
        exName: "Plank nghiêng (Side Plank)",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo3.jpg?updatedAt=1742350767390",
        equipment: "Không cần thiết bị",
        perform: "Nằm nghiêng, chống người lên bằng một khuỷu tay và cạnh ngoài của bàn chân. Giữ thân thẳng trong 30 giây, sau đó đổi bên. Lặp lại 2 lần mỗi bên.",
        rep: "Giữ 30 giây x 2 lần mỗi bên",
        rest: "30 giây",
      },
    ],
  },
];

export const infoBodyDataBack = [
  {
    x: 180,
    y: 150,
    placement: "left",
    label: "posterior deltoid",
    name: "Cơ vai phía sau",
    image: "https://ik.imagekit.io/dgeniust/posterior%20deltoid.jpg?updatedAt=1742351373227",
    content:
    "Cơ Vai Phía Sau (Posterior Deltoid): là nhóm cơ cuối cùng, nằm ở phía sau vai và kéo dài từ xương bả vai (scapula) đến xương cánh tay (humerus). Cơ này không chỉ giúp duỗi cánh tay ra sau mà còn hỗ trợ việc mở rộng cánh tay ra hai bên, từ đó nâng cao khả năng di chuyển và độ linh hoạt của vai.",
    function: "Giúp duỗi cánh tay ra sau mà còn hỗ trợ việc mở rộng cánh tay hai bên.",
    sick: ["Hội chứng bắt chẹn vai", "Thoái hoá khớp vai", "Mất vững ở khớp vai"],
    exercise: [
      {
        exName: "Kéo cáp chéo",
        exIMG: "https://ik.imagekit.io/dgeniust/exVaiSau1.png?updatedAt=1742350767983", // Thêm ảnh nếu có
        equipment: "Máy kéo cáp",
        perform: "Đứng thẳng, tay cầm tay cầm cáp chéo. Kéo tay cầm về phía đối diện của cơ thể, xoay thân người. Lặp lại 10 lần mỗi bên.",
        rep: "10 lần",
        rest: "30 giây",
      },
      {
        exName: "Bay vai sau",
        exIMG: "https://ik.imagekit.io/dgeniust/exVaiSau2.png?updatedAt=1742350768104",
        equipment: "Tạ đơn hoặc máy bay vai",
        perform: "Đứng hoặc ngồi, cúi người về phía trước, tay cầm tạ đơn. Nâng tạ sang hai bên, giữ khuỷu tay hơi cong. Hạ tạ từ từ. Lặp lại 12 lần.",
        rep: "12 lần",
        rest: "30 giây",
      },
      {
        exName: "Kéo cáp ngang mặt",
        exIMG: "https://ik.imagekit.io/dgeniust/exVaiSau3.png?updatedAt=1742350771363", // Thay thế bằng ảnh phù hợp
        equipment: "Máy kéo cáp",
        perform: "Đứng thẳng, tay cầm tay cầm cáp ngang mặt. Kéo tay cầm về phía mặt, giữ khuỷu tay cao. Hạ tay cầm từ từ. Lặp lại 12 lần.",
        rep: "12 lần",
        rest: "30 giây",
      },
      // Thêm các bài tập khác
    ],
  },
  {
    x: 335,
    y: 180,
    placement: "right",
    label: "rhomboids",
    name: "Cơ trám",
    image: "https://ik.imagekit.io/dgeniust/rhomboids.png?updatedAt=1742351458815",
    content:
      "Rhomboids (hay còn gọi là cơ trám) là một nhóm cơ được tạo thành từ Rhomboid major (Cơ trám lớn) và Rhomboid minor (Cơ trám bé). Rhomboids rất quan trọng trong việc giữa ổn định của đai vai và xương bả vai, đồng thời ảnh hưởng đến vận động của chi trên. Trong khi hiếm có ca phẫu thuật nào liên quan đến Rhomboids, nhưng hội chứng Winged Scapula và Rhomboid Palsy có liên quan đến cơ này.",
      function : 'Rhomboids thực hiện các chuyển động khép, nâng nhẹ và xoay xương bả vai xuống dưới. Romboids hoạt động cùng với cơ Levator Scapulae và Trapezius để khép và nâng xương bả vai. Đó là lý do thì sao khi chúng ta thực hiện retract quá mức ở các bài tập như Rowing hay Pressing, vai của chúng ta bị shrugs lên là vậy.',
    sick: ['Đau cơ bả vai', 'Hội chứng Winged Scapula', 'Rhomboid Palsy' ],
    exercise: [
      {
        exName: "Kéo cáp ngồi (Seated Cable Rows)",
        exIMG: "https://ik.imagekit.io/dgeniust/exTram1.jpg?updatedAt=1742359810816", // Thay thế bằng ảnh phù hợp
        equipment: "Máy kéo cáp",
        perform: "Ngồi trên máy kéo cáp, chân đặt vững trên sàn, tay nắm thanh kéo. Kéo thanh về phía bụng, siết chặt cơ trám, giữ trong 1 giây. Từ từ thả tay về vị trí ban đầu. Lặp lại 12 lần.",
        rep: "12 lần",
        rest: "30 giây",
      },
      {
        exName: "Kéo tạ chữ T (T-Bar Rows)",
        exIMG: "https://ik.imagekit.io/dgeniust/exTram2.jpg?updatedAt=1742359854316", // Thay thế bằng ảnh phù hợp
        equipment: "Tạ chữ T",
        perform: "Đứng hoặc quỳ, tay nắm tạ chữ T. Kéo tạ về phía ngực, siết chặt cơ trám, giữ trong 1 giây. Từ từ hạ tạ về vị trí ban đầu. Lặp lại 10 lần.",
        rep: "10 lần",
        rest: "30 giây",
      },
      {
        exName: "Flyes ngược (Reverse Flyes)",
        exIMG: "https://ik.imagekit.io/dgeniust/exTram3.webp?updatedAt=1742359893349", // Thay thế bằng ảnh phù hợp
        equipment: "Tạ đơn hoặc máy bay vai ngược",
        perform: "Đứng hoặc ngồi, cúi người về phía trước, tay cầm tạ đơn. Nâng tạ sang hai bên, siết chặt cơ trám, giữ trong 1 giây. Từ từ hạ tạ về vị trí ban đầu. Lặp lại 12 lần.",
        rep: "12 lần",
        rest: "30 giây",
      },
      {
        exName: "Face Pulls",
        exIMG: "https://ik.imagekit.io/dgeniust/exTram4.jpg?updatedAt=1742359955161", // Thay thế bằng ảnh phù hợp
        equipment: "Máy kéo cáp",
        perform: "Đứng thẳng, tay nắm dây cáp kéo về phía mặt. Kéo dây về phía mặt, xoay hai tay ra ngoài, siết chặt cơ trám. Từ từ thả dây về vị trí ban đầu. Lặp lại 15 lần.",
        rep: "15 lần",
        rest: "30 giây",
      },
    ],
  },
  {
    x: 200,
    y: 370,
    placement: "top",
    label: "external oblique",
    name: "Cơ ngoại chéo",
    image: "https://ik.imagekit.io/dgeniust/external.jpg?updatedAt=1742350767480",
    content:
      "External oblique: Nhóm cơ này nằm xung quanh cơ rectus abdominis. Chúng giúp ổn định cơ thể nếu bị xoay hoặc vặn.",
    function: "Giúp xoay thân, nghiêng thân sang một bên và gập thân. Tham gia vào quá trình thở ra mạnh và duy trì áp lực ổ bụng.",
    sick: ["Đau sườn", "Căng cơ liên sườn", "Đau lưng dưới"],
    exercise: [
      {
        exName: "Nghiêng người với tạ tay",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo1.jpg?updatedAt=1742350763281", // Thêm ảnh nếu có
        equipment: "Tạ tay",
        perform: "Đứng thẳng, mỗi tay cầm một quả tạ. Nghiêng người sang phải, giữ 2 giây, rồi nghiêng người sang trái, giữ 2 giây. Lặp lại 12 lần mỗi bên.",
        rep: "12 lần mỗi bên",
        rest: "30 giây",
      },
      {
        exName: "Vặn người kiểu Nga (Russian Twists)",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo2.jpg?updatedAt=1742350763254",
        equipment: "Không cần thiết bị (có thể dùng tạ hoặc bóng)",
        perform: "Ngồi trên sàn, đầu gối hơi cong, chân có thể đặt trên sàn hoặc nhấc lên. Giữ thẳng lưng và vặn thân sang trái, rồi sang phải. Lặp lại 15 lần mỗi bên.",
        rep: "15 lần mỗi bên",
        rest: "30 giây",
      },
      {
        exName: "Plank nghiêng (Side Plank)",
        exIMG: "https://ik.imagekit.io/dgeniust/exCoCheo3.jpg?updatedAt=1742350767390",
        equipment: "Không cần thiết bị",
        perform: "Nằm nghiêng, chống người lên bằng một khuỷu tay và cạnh ngoài của bàn chân. Giữ thân thẳng trong 30 giây, sau đó đổi bên. Lặp lại 2 lần mỗi bên.",
        rep: "Giữ 30 giây x 2 lần mỗi bên",
        rest: "30 giây",
      },
    ],
    
  },
  {
    x: 350,
    y: 310,
    placement: "right",
    label: "latissimus dorsi",
    name: "Cơ xô",
    image: "https://ik.imagekit.io/dgeniust/latissimus.PNG?updatedAt=1742351809717",
    content:
      "Cơ xô (Lat hoặc Latissimus Dorsi). Bao gồm 2 cơ lớn nằm dưới nách giáp phía trên là lưng giữa (Middle Back) và phía bên là cơ cầu vai (Traps).",
      function: "Gập bụng, hỗ trợ hô hấp, bảo vệ nội tạng.",
    sick: ["Đau lưng dưới", "Căng cơ xô", "Hạn chế vận động cánh tay"],
    exercise: [
      {
        exName: "Kéo xà đơn (Pull-ups)",
        exIMG: "https://ik.imagekit.io/dgeniust/exXo1.png?updatedAt=1742351730454", // Thay thế bằng hình ảnh bài tập
        equipment: "Xà đơn",
        perform: "Treo người trên xà đơn, tay rộng hơn vai. Kéo người lên cho đến khi cằm vượt qua xà. Hạ người xuống từ từ. Lặp lại 8-12 lần.",
        rep: "8-12 lần",
        rest: "60 giây",
      },
      {
        exName: "Kéo cáp rộng tay (Wide-grip Lat Pulldowns)",
        exIMG: "https://ik.imagekit.io/dgeniust/exXo2.webp?updatedAt=1742351730566", // Thay thế bằng hình ảnh bài tập
        equipment: "Máy kéo cáp",
        perform: "Ngồi trên máy kéo cáp, tay nắm thanh kéo rộng hơn vai. Kéo thanh kéo xuống ngực trên. Từ từ thả thanh kéo về vị trí ban đầu. Lặp lại 10-15 lần.",
        rep: "10-15 lần",
        rest: "45 giây",
      },
      {
        exName: "Kéo tạ đòn (Barbell Rows)",
        exIMG: "https://ik.imagekit.io/dgeniust/exXo3.jpg?updatedAt=1742360169611", // Thay thế bằng hình ảnh bài tập
        equipment: "Tạ đòn",
        perform: "Cúi người về phía trước, lưng thẳng, tay nắm tạ đòn. Kéo tạ về phía bụng, siết chặt cơ xô. Từ từ hạ tạ về vị trí ban đầu. Lặp lại 8-12 lần.",
        rep: "8-12 lần",
        rest: "60 giây",
      },
    ],
  },
  {
    x: 360,
    y: 450,
    placement: "top",
    label: "serratus anterior",
    name: "Cơ mông",
    image: "https://ik.imagekit.io/dgeniust/serratus-anterior.jpg?updatedAt=1742360411392",
    content:
      "Cơ mông có ba cơ chính tạo nên nhóm cơ glutes, bao gồm cơ mông lớn (gluteus maximus), cơ mông nhỡ (gluteus medius) và cơ mông nhỏ (gluteus minimus).Ngoài ra còn có một lớp mỡ nằm phía trên các cơ này.",
      function: "Kéo xương vai ra trước, xoay và giữ xương vai áp vào thành ngực.",
    sick: ["Đau vai", "Khó cử động vai"],
    exercise: [
      {
        exName: "Squat",
        exIMG: "https://ik.imagekit.io/dgeniust/exMong1.jpg?updatedAt=1742360244494", // Thêm ảnh nếu có
        equipment: "Không cần thiết bị",
        perform: "Đứng hai chân rộng bằng vai, đầu gối hơi cong. Từ từ hạ thấp mông như thể ngồi ngả lưng trên ghế. Lưu ý: cần cố gắng giữ cho đầu gối của bạn không di chuyển về phía trước. Sau đó trở về tư thế đứng ban đầu.",
        rep: "10 lần",
        rest: "30 giây",
      },
      {
        exName: "Squat bóng",
        exIMG: "https://ik.imagekit.io/dgeniust/exMong2.jpg?updatedAt=1742360244594",
        equipment: "Bóng",
        perform: "Tư thế chuẩn bị: lưng quay vào tường, đặt bóng giữa phần thắt lưng của bạn và tường. Từ từ thực hiện động tác squat cổ điển như trên. Bước chân ra phía trước, để đầu gối ở phía sau ngón chân của bạn. Ngồi xổm với lưng của bạn luôn tiếp xúc với quả bóng.",
        rep: "15 lần",
        rest: "30 giây",
      },
      // Thêm các bài tập khác
    ],
  },
];

export default infoBodyData;
