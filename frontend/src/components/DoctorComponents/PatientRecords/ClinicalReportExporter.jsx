import React from 'react';
import { Button, Space } from 'antd';
import { FilePdfOutlined } from '@ant-design/icons';
import { Document, Page, Text, View, StyleSheet, PDFDownloadLink, Font, Image } from '@react-pdf/renderer';

// Đăng ký font (để hỗ trợ tiếng Việt tốt hơn)
Font.register({
  family: 'Roboto',
  fonts: [
    { src: 'https://cdnjs.cloudflare.com/ajax/libs/ink/3.1.10/fonts/Roboto/roboto-regular-webfont.ttf' },
    { src: 'https://cdnjs.cloudflare.com/ajax/libs/ink/3.1.10/fonts/Roboto/roboto-bold-webfont.ttf', fontWeight: 'bold' },
  ]
});

// Định nghĩa styles cho PDF
const styles = StyleSheet.create({
  page: {
    padding: 30,
    fontFamily: 'Roboto',
    fontSize: 12,
  },
  header: {
    marginBottom: 20,
  },
  nationalTitle: {
    fontSize: 12,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 3,
  },
  nationalMotto: {
    fontSize: 11,
    textAlign: 'center',
    marginBottom: 15,
  },
  headerLine: {
    borderBottom: 1,
    borderBottomColor: '#000',
    borderBottomStyle: 'solid',
    marginBottom: 15,
  },
  hospitalInfo: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  hospitalName: {
    fontSize: 11,
    fontWeight: 'bold',
  },
  documentNumber: {
    fontSize: 10,
  },
  title: {
    fontSize: 18,
    marginBottom: 10,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  subtitle: {
    fontSize: 14,
    marginTop: 15,
    marginBottom: 10,
    fontWeight: 'bold',
    backgroundColor: '#f0f0f0',
    padding: 5,
  },
  table: {
    display: 'table',
    width: 'auto',
    borderStyle: 'solid',
    borderWidth: 1,
    borderColor: '#bfbfbf',
    marginBottom: 20,
  },
  tableRow: {
    flexDirection: 'row',
  },
  tableRowHeader: {
    flexDirection: 'row',
    backgroundColor: '#f0f0f0',
  },
  tableCol: {
    borderStyle: 'solid',
    borderWidth: 1,
    borderColor: '#bfbfbf',
  },
  tableCell: {
    padding: 5,
    fontSize: 10,
  },
  card: {
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    padding: 5,
  },
  imageCard: {
    flexDirection: 'row',
    marginBottom: 10,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    padding: 5,
  },
  imageCardInfo: {
    flex: 1,
  },
  imagePlaceholder: {
    width: 80,
    height: 60,
    backgroundColor: '#f0f0f0',
    marginRight: 10,
    justifyContent: 'center',
    alignItems: 'center',
  },
  row: {
    flexDirection: 'row',
    marginBottom: 3,
  },
  label: {
    width: 100,
    fontSize: 10,
  },
  value: {
    flex: 1,
    fontSize: 10,
  },
  footer: {
    marginTop: 30,
    fontSize: 10,
    color: 'grey',
  },
  patientInfo: {
    marginBottom: 20,
    padding: 10,
    borderWidth: 1,
    borderColor: '#e0e0e0',
  },
  listItem: {
    marginBottom: 5,
    flexDirection: 'row',
  },
  statusBadge: {
    width: 8,
    height: 8,
    borderRadius: 4,
    marginRight: 5,
  },
  signaturesSection: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginTop: 30,
  },
  signatureBox: {
    width: '45%',
    alignItems: 'center',
  },
  signatureTitle: {
    fontSize: 11,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  signatureDate: {
    fontSize: 10,
    marginBottom: 30, // Khoảng trống để ký
  },
  signatureName: {
    fontSize: 10,
    fontWeight: 'bold',
    marginTop: 10,
  },
  watermark: {
    position: 'absolute',
    top: '50%',
    left: '25%',
    opacity: 0.08,
    transform: 'rotate(-45deg)',
    fontSize: 60,
  },
});

// Tiêu đề quốc gia và bệnh viện tái sử dụng
const HeaderSection = ({ hospital = "BỆNH VIỆN XYZ", documentNumber = "" }) => {
  const today = new Date();
  const formattedDate = `${today.getDate()}/${today.getMonth() + 1}/${today.getFullYear()}`;
  
  return (
    <View style={styles.header}>
      <Text style={styles.nationalTitle}>CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM</Text>
      <Text style={styles.nationalMotto}>Độc lập - Tự do - Hạnh phúc</Text>
      <View style={styles.headerLine} />
      <View style={styles.hospitalInfo}>
        <Text style={styles.hospitalName}>{hospital}</Text>
        <Text style={styles.documentNumber}>Số: {documentNumber || `....../${today.getFullYear()}`}</Text>
      </View>
    </View>
  );
};

// Component thông tin bệnh nhân tái sử dụng
const PatientInfoSection = ({ patientInfo }) => (
  <View style={styles.patientInfo}>
    <View style={styles.row}>
      <Text style={styles.label}>Họ và tên:</Text>
      <Text style={styles.value}>{patientInfo?.name || 'N/A'}</Text>
    </View>
    <View style={styles.row}>
      <Text style={styles.label}>Mã bệnh nhân:</Text>
      <Text style={styles.value}>{patientInfo?.id || 'N/A'}</Text>
    </View>
    <View style={styles.row}>
      <Text style={styles.label}>Ngày sinh:</Text>
      <Text style={styles.value}>{patientInfo?.dob || 'N/A'}</Text>
    </View>
    <View style={styles.row}>
      <Text style={styles.label}>Giới tính:</Text>
      <Text style={styles.value}>{patientInfo?.gender || 'N/A'}</Text>
    </View>
    <View style={styles.row}>
      <Text style={styles.label}>Địa chỉ:</Text>
      <Text style={styles.value}>{patientInfo?.address || 'N/A'}</Text>
    </View>
    <View style={styles.row}>
      <Text style={styles.label}>Bác sĩ phụ trách:</Text>
      <Text style={styles.value}>{patientInfo?.doctor || 'N/A'}</Text>
    </View>
  </View>
);

// Component chữ ký tái sử dụng
const SignaturesSection = ({ doctorName = "", patientName = "" }) => {
  const today = new Date();
  const day = today.getDate();
  const month = today.getMonth() + 1;
  const year = today.getFullYear();
  
  return (
    <View style={styles.signaturesSection}>
      <View style={styles.signatureBox}>
        <Text style={styles.signatureTitle}>NGƯỜI BỆNH</Text>
        <Text style={styles.signatureDate}>(Ký, ghi rõ họ tên)</Text>
        <Text style={styles.signatureName}>{patientName}</Text>
      </View>
      <View style={styles.signatureBox}>
        <Text style={styles.signatureTitle}>BÁC SĨ KHÁM BỆNH</Text>
        <Text style={styles.signatureDate}>Ngày {day} tháng {month} năm {year}</Text>
        <Text style={styles.signatureName}>{doctorName}</Text>
      </View>
    </View>
  );
};

// Component footer tái sử dụng
const FooterSection = () => {
  const today = new Date();
  return (
    <View style={styles.footer}>
      <Text>Báo cáo được tạo vào {today.toLocaleDateString('vi-VN')} - {today.toLocaleTimeString('vi-VN')}</Text>
      <Text>Kết quả này chỉ có giá trị tại bệnh viện. Mọi sao chép đều không có giá trị pháp lý.</Text>
    </View>
  );
};

// Component PDF cho xét nghiệm (Lab Tests)
const LabTestsPDF = ({ labTests, patientInfo }) => (
  <Document>
    <Page size="A4" style={styles.page}>
      <Text style={styles.watermark}>BẢN SAO Y</Text>
      <HeaderSection />
      <Text style={styles.title}>BÁO CÁO KẾT QUẢ XÉT NGHIỆM</Text>
      <PatientInfoSection patientInfo={patientInfo} />
      
      <Text style={styles.subtitle}>Kết quả xét nghiệm</Text>
      <View style={styles.table}>
        <View style={styles.tableRowHeader}>
          <View style={[styles.tableCol, { width: '25%' }]}>
            <Text style={styles.tableCell}>Tên xét nghiệm</Text>
          </View>
          <View style={[styles.tableCol, { width: '15%' }]}>
            <Text style={styles.tableCell}>Loại</Text>
          </View>
          <View style={[styles.tableCol, { width: '20%' }]}>
            <Text style={styles.tableCell}>Thời gian yêu cầu</Text>
          </View>
          <View style={[styles.tableCol, { width: '15%' }]}>
            <Text style={styles.tableCell}>Kết quả</Text>
          </View>
          <View style={[styles.tableCol, { width: '15%' }]}>
            <Text style={styles.tableCell}>Đơn vị</Text>
          </View>
          <View style={[styles.tableCol, { width: '10%' }]}>
            <Text style={styles.tableCell}>Trạng thái</Text>
          </View>
        </View>
        {labTests.map((test, index) => (
          <View key={index} style={styles.tableRow}>
            <View style={[styles.tableCol, { width: '25%' }]}>
              <Text style={styles.tableCell}>{test.name}</Text>
            </View>
            <View style={[styles.tableCol, { width: '15%' }]}>
              <Text style={styles.tableCell}>{test.type}</Text>
            </View>
            <View style={[styles.tableCol, { width: '20%' }]}>
              <Text style={styles.tableCell}>{test.requestDate}</Text>
            </View>
            <View style={[styles.tableCol, { width: '15%' }]}>
              <Text style={styles.tableCell}>{test.status === 'Hoàn thành' ? (test.result || 'Bình thường') : 'Chưa có'}</Text>
            </View>
            <View style={[styles.tableCol, { width: '15%' }]}>
              <Text style={styles.tableCell}>{test.unit || '-'}</Text>
            </View>
            <View style={[styles.tableCol, { width: '10%' }]}>
              <Text style={styles.tableCell}>{test.status}</Text>
            </View>
          </View>
        ))}
      </View>
      
      <View style={styles.subtitle}>
        <Text>Ghi chú:</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.value}>Các kết quả xét nghiệm trên chỉ có giá trị tham khảo và cần được đánh giá bởi bác sĩ chuyên khoa.</Text>
      </View>
      
      <SignaturesSection 
        doctorName={patientInfo?.doctor || "Bs. ........................"} 
        patientName={patientInfo?.name || "........................"} 
      />
      
      <FooterSection />
    </Page>
  </Document>
);

// Component PDF cho chẩn đoán hình ảnh (Imaging Tests)
const ImagingTestsPDF = ({ imagingTests, patientInfo }) => (
  <Document>
    <Page size="A4" style={styles.page}>
      <Text style={styles.watermark}>BẢN SAO Y</Text>
      <HeaderSection />
      <Text style={styles.title}>BÁO CÁO CHẨN ĐOÁN HÌNH ẢNH</Text>
      <PatientInfoSection patientInfo={patientInfo} />
      
      <Text style={styles.subtitle}>Kết quả chẩn đoán hình ảnh</Text>
      {imagingTests.map((test, index) => (
        <View key={index} style={styles.imageCard}>
          <View style={styles.imagePlaceholder}>
            <Text style={{ fontSize: 8, textAlign: 'center' }}>
              {test.status === 'Hoàn thành' ? 'Đã có hình' : 'Chưa có hình'}
            </Text>
          </View>
          <View style={styles.imageCardInfo}>
            <Text style={{ fontWeight: 'bold', fontSize: 10 }}>{test.name}</Text>
            <View style={styles.row}>
              <Text style={[styles.label, { fontSize: 8 }]}>Ngày yêu cầu:</Text>
              <Text style={[styles.value, { fontSize: 8 }]}>{test.requestDate}</Text>
            </View>
            <View style={styles.row}>
              <Text style={[styles.label, { fontSize: 8 }]}>Bác sĩ chỉ định:</Text>
              <Text style={[styles.value, { fontSize: 8 }]}>{test.requestedBy || patientInfo?.doctor}</Text>
            </View>
            <View style={styles.row}>
              <View style={[styles.statusBadge, { backgroundColor: test.status === 'Hoàn thành' ? '#52c41a' : '#1890ff' }]} />
              <Text style={[styles.value, { fontSize: 8 }]}>{test.status}</Text>
            </View>
          </View>
        </View>
      ))}
      
      <View style={styles.subtitle}>
        <Text>Kết luận:</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.value}>................................................................................................................</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.value}>................................................................................................................</Text>
      </View>
      
      <SignaturesSection 
        doctorName={patientInfo?.doctor || "Bs. ........................"} 
        patientName={patientInfo?.name || "........................"} 
      />
      
      <FooterSection />
    </Page>
  </Document>
);

// Component PDF cho thăm dò chức năng (Functional Tests)
const FunctionalTestsPDF = ({ functionalTests, patientInfo }) => (
  <Document>
    <Page size="A4" style={styles.page}>
      <Text style={styles.watermark}>BẢN SAO Y</Text>
      <HeaderSection />
      <Text style={styles.title}>BÁO CÁO THĂM DÒ CHỨC NĂNG</Text>
      <PatientInfoSection patientInfo={patientInfo} />
      
      <Text style={styles.subtitle}>Kết quả thăm dò chức năng</Text>
      <View style={styles.table}>
        <View style={styles.tableRowHeader}>
          <View style={[styles.tableCol, { width: '30%' }]}>
            <Text style={styles.tableCell}>Tên xét nghiệm</Text>
          </View>
          <View style={[styles.tableCol, { width: '15%' }]}>
            <Text style={styles.tableCell}>Loại</Text>
          </View>
          <View style={[styles.tableCol, { width: '20%' }]}>
            <Text style={styles.tableCell}>Ngày yêu cầu</Text>
          </View>
          <View style={[styles.tableCol, { width: '15%' }]}>
            <Text style={styles.tableCell}>Bác sĩ chỉ định</Text>
          </View>
          <View style={[styles.tableCol, { width: '20%' }]}>
            <Text style={styles.tableCell}>Trạng thái</Text>
          </View>
        </View>
        {functionalTests.map((test, index) => (
          <View key={index} style={styles.tableRow}>
            <View style={[styles.tableCol, { width: '30%' }]}>
              <Text style={styles.tableCell}>{test.name}</Text>
            </View>
            <View style={[styles.tableCol, { width: '15%' }]}>
              <Text style={styles.tableCell}>{test.type}</Text>
            </View>
            <View style={[styles.tableCol, { width: '20%' }]}>
              <Text style={styles.tableCell}>{test.requestDate}</Text>
            </View>
            <View style={[styles.tableCol, { width: '15%' }]}>
              <Text style={styles.tableCell}>{test.requestedBy}</Text>
            </View>
            <View style={[styles.tableCol, { width: '20%' }]}>
              <Text style={styles.tableCell}>{test.status}</Text>
            </View>
          </View>
        ))}
      </View>
      
      <View style={styles.subtitle}>
        <Text>Nhận xét tổng quát:</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.value}>................................................................................................................</Text>
      </View>
      <View style={styles.row}>
        <Text style={styles.value}>................................................................................................................</Text>
      </View>
      
      <SignaturesSection 
        doctorName={patientInfo?.doctor || "Bs. ........................"} 
        patientName={patientInfo?.name || "........................"} 
      />
      
      <FooterSection />
    </Page>
  </Document>
);

// Component PDF tổng hợp tất cả báo cáo
const ComprehensiveReportPDF = ({ labTests, imagingTests, functionalTests, patientInfo }) => (
  <Document>
    <Page size="A4" style={styles.page}>
      <Text style={styles.watermark}>BẢN SAO Y</Text>
      <HeaderSection />
      <Text style={styles.title}>BÁO CÁO KẾT QUẢ TỔNG HỢP</Text>
      <PatientInfoSection patientInfo={patientInfo} />
      
      <Text style={styles.subtitle}>Kết quả xét nghiệm</Text>
      <View style={styles.table}>
        <View style={styles.tableRowHeader}>
          <View style={[styles.tableCol, { width: '40%' }]}>
            <Text style={styles.tableCell}>Tên xét nghiệm</Text>
          </View>
          <View style={[styles.tableCol, { width: '25%' }]}>
            <Text style={styles.tableCell}>Ngày yêu cầu</Text>
          </View>
          <View style={[styles.tableCol, { width: '35%' }]}>
            <Text style={styles.tableCell}>Trạng thái</Text>
          </View>
        </View>
        {labTests.slice(0, 3).map((test, index) => (
          <View key={index} style={styles.tableRow}>
            <View style={[styles.tableCol, { width: '40%' }]}>
              <Text style={styles.tableCell}>{test.name}</Text>
            </View>
            <View style={[styles.tableCol, { width: '25%' }]}>
              <Text style={styles.tableCell}>{test.requestDate}</Text>
            </View>
            <View style={[styles.tableCol, { width: '35%' }]}>
              <Text style={styles.tableCell}>{test.status}</Text>
            </View>
          </View>
        ))}
      </View>
      
      <Text style={styles.subtitle}>Chẩn đoán hình ảnh</Text>
      <View style={styles.table}>
        <View style={styles.tableRowHeader}>
          <View style={[styles.tableCol, { width: '40%' }]}>
            <Text style={styles.tableCell}>Tên xét nghiệm</Text>
          </View>
          <View style={[styles.tableCol, { width: '25%' }]}>
            <Text style={styles.tableCell}>Ngày yêu cầu</Text>
          </View>
          <View style={[styles.tableCol, { width: '35%' }]}>
            <Text style={styles.tableCell}>Trạng thái</Text>
          </View>
        </View>
        {imagingTests.slice(0, 3).map((test, index) => (
          <View key={index} style={styles.tableRow}>
            <View style={[styles.tableCol, { width: '40%' }]}>
              <Text style={styles.tableCell}>{test.name}</Text>
            </View>
            <View style={[styles.tableCol, { width: '25%' }]}>
              <Text style={styles.tableCell}>{test.requestDate}</Text>
            </View>
            <View style={[styles.tableCol, { width: '35%' }]}>
              <Text style={styles.tableCell}>{test.status}</Text>
            </View>
          </View>
        ))}
      </View>
      
      <Text style={styles.subtitle}>Thăm dò chức năng</Text>
      <View style={styles.table}>
        <View style={styles.tableRowHeader}>
          <View style={[styles.tableCol, { width: '40%' }]}>
            <Text style={styles.tableCell}>Tên xét nghiệm</Text>
          </View>
          <View style={[styles.tableCol, { width: '25%' }]}>
            <Text style={styles.tableCell}>Ngày yêu cầu</Text>
          </View>
          <View style={[styles.tableCol, { width: '35%' }]}>
            <Text style={styles.tableCell}>Trạng thái</Text>
          </View>
        </View>
        {functionalTests.slice(0, 3).map((test, index) => (
          <View key={index} style={styles.tableRow}>
            <View style={[styles.tableCol, { width: '40%' }]}>
              <Text style={styles.tableCell}>{test.name}</Text>
            </View>
            <View style={[styles.tableCol, { width: '25%' }]}>
              <Text style={styles.tableCell}>{test.requestDate}</Text>
            </View>
            <View style={[styles.tableCol, { width: '35%' }]}>
              <Text style={styles.tableCell}>{test.status}</Text>
            </View>
          </View>
        ))}
      </View>
      
      <SignaturesSection 
        doctorName={patientInfo?.doctor || "Bs. ........................"} 
        patientName={patientInfo?.name || "........................"} 
      />
      
      <FooterSection />
    </Page>
  </Document>
);

// Component button xuất từng loại PDF
const PDFExportButtons = ({ labTests, imagingTests, functionalTests }) => {
  // Thông tin mẫu về bệnh nhân (bạn có thể thay thế bằng dữ liệu thực tế)
  const patientInfo = {
    name: 'Nguyễn Văn A',
    id: 'BN-123456',
    dob: '01/01/1980',
    gender: 'Nam',
    address: 'Số 123 Đường ABC, Phường XYZ, Quận/Huyện, Tỉnh/Thành phố',
    doctor: 'Bs. Trần Văn B'
  };

  return (
    <Space>
      <PDFDownloadLink
        document={<LabTestsPDF labTests={labTests} patientInfo={patientInfo} />}
        fileName={`${patientInfo.id}-bao-cao-xet-nghiem.pdf`}
        style={{ textDecoration: 'none' }}
      >
        {({ blob, url, loading, error }) => (
          <Button
            icon={<FilePdfOutlined />}
            loading={loading}
            disabled={error}
          >
            {loading ? 'Đang tạo...' : 'Xuất XN'}
          </Button>
        )}
      </PDFDownloadLink>

      <PDFDownloadLink
        document={<ImagingTestsPDF imagingTests={imagingTests} patientInfo={patientInfo} />}
        fileName={`${patientInfo.id}-bao-cao-chan-doan-hinh-anh.pdf`}
        style={{ textDecoration: 'none' }}
      >
        {({ blob, url, loading, error }) => (
          <Button
            icon={<FilePdfOutlined />}
            loading={loading}
            disabled={error}
          >
            {loading ? 'Đang tạo...' : 'Xuất CDHA'}
          </Button>
        )}
      </PDFDownloadLink>

      <PDFDownloadLink
        document={<FunctionalTestsPDF functionalTests={functionalTests} patientInfo={patientInfo} />}
        fileName={`${patientInfo.id}-bao-cao-tham-do-chuc-nang.pdf`}
        style={{ textDecoration: 'none' }}
      >
        {({ blob, url, loading, error }) => (
          <Button
            icon={<FilePdfOutlined />}
            loading={loading}
            disabled={error}
          >
            {loading ? 'Đang tạo...' : 'Xuất TĐCN'}
          </Button>
        )}
      </PDFDownloadLink>

      <PDFDownloadLink
        document={<ComprehensiveReportPDF 
          labTests={labTests} 
          imagingTests={imagingTests} 
          functionalTests={functionalTests}
          patientInfo={patientInfo} 
        />}
        fileName={`${patientInfo.id}-bao-cao-tong-hop.pdf`}
        style={{ textDecoration: 'none' }}
      >
        {({ blob, url, loading, error }) => (
          <Button
            icon={<FilePdfOutlined />}
            loading={loading}
            disabled={error}
            type="primary"
          >
            {loading ? 'Đang tạo...' : 'Xuất Tổng Hợp'}
          </Button>
        )}
      </PDFDownloadLink>
    </Space>
  );
};

// Component chính để sử dụng trong ứng dụng của bạn
const ClinicalReportExporter = ({ labTests, imagingTests, functionalTests }) => {
  return (
    <div className="mt-4 flex justify-end">
      <PDFExportButtons 
        labTests={labTests} 
        imagingTests={imagingTests} 
        functionalTests={functionalTests} 
      />
    </div>
  );
};

export default ClinicalReportExporter;