import { UiPageModule } from './ui-page.module';

describe('UiPageModule', () => {
  let uiPageModule: UiPageModule;

  beforeEach(() => {
    uiPageModule = new UiPageModule();
  });

  it('should create an instance', () => {
    expect(uiPageModule).toBeTruthy();
  });
});
